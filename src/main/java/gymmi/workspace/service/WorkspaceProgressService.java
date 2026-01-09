package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.eventlistener.event.*;
import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.service.ImageUse;
import gymmi.workspace.service.domain.objection.ObjectionAlreadyOpenValidator;
import gymmi.workspace.service.domain.objection.ObjectionStarter;
import gymmi.workspace.service.domain.vote.VoteManger;
import gymmi.workspace.service.domain.vote.VoteReflector;
import gymmi.workspace.service.domain.vote.VoteValidator;
import gymmi.workspace.service.domain.workout.*;
import gymmi.workspace.service.domain.workspace.WorkspaceDrawManger;
import gymmi.workspace.service.domain.objection.Objection;
import gymmi.workspace.service.domain.vote.Vote;
import gymmi.workspace.service.domain.workspace.Worker;
import gymmi.workspace.service.domain.workspace.Workspace;
import gymmi.workspace.service.domain.workspace.WorkspaceResult;
import gymmi.workspace.repository.*;
import gymmi.workspace.request.WorkoutRequest;
import gymmi.workspace.response.WorkspaceResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkspaceProgressService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final MissionRepository missionRepository;
    private final WorkoutHistoryRepository workoutHistoryRepository;
    private final FavoriteMissionRepository favoriteMissionRepository;
    private final ObjectionRepository objectionRepository;
    private final VoteRepository voteRepository;
    private final WorkspaceResultRepository workspaceResultRepository;
    private final WorkoutValidator workoutValidator;
    private final WorkoutRequestMapper workoutRequestMapper;
    private final ObjectionAlreadyOpenValidator objectionAlreadyOpenValidator;
    private final VoteValidator voteValidator;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final WorkoutRecordRepository workoutRecordRepository;

    @Transactional // 동시성 문제
    public Integer workMissionsInWorkspace(
            User loginedUser,
            Long workspaceId,
            WorkoutRequest workoutRequest
    ) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspace.getId());

        // 이미지 검사 다른 방식 필요
        applicationEventPublisher.publishEvent(new ImageValidationEvent(ImageUse.WORKOUT_CONFIRMATION, workoutRequest.getImageUrl()));

        WorkoutConfirmation workoutConfirmation = workoutRequestMapper.createWorkoutConfirmation(workoutRequest);
        WorkoutHistory workoutHistory = new WorkoutHistory(worker, workoutConfirmation);
        workoutHistoryRepository.save(workoutHistory);
        List<WorkoutRecord> workoutRecords = workoutRequestMapper.createWorkoutRecords(workspace, workoutHistory, workoutRequest.getMissions());
        workoutRecordRepository.saveAll(workoutRecords);

        WorkoutProcessor workoutProcessor = new WorkoutProcessor(workspace, worker, workoutRecords);
        workoutProcessor.apply(workoutValidator);

        applicationEventPublisher.publishEvent(new WorkoutConfirmationCreatedEvent(workspace.getId(), loginedUser.getId()));
        if (workoutProcessor.isPhaseChanged()) {
            applicationEventPublisher.publishEvent(new WorkspacePhaseChangedEvent(workspace.getId(), workoutProcessor.getWorkspacePhase()));
        }
        if (workoutRequest.getWillLink()) {
            applicationEventPublisher.publishEvent(new LinkToPhotoFeedEvent(loginedUser.getId(), workoutRequest.getImageUrl(), workoutRequest.getComment()));
        }

        return workoutProcessor.getSumScore();
    }


    public void objectToWorkoutHistory(User loginedUser, Long workspaceId, Long workoutConfirmationId, String reason) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspaceId);
        WorkoutHistory workoutHistory = workoutHistoryRepository.findByWorkoutConfirmationIdOrThrow(workoutConfirmationId);

        ObjectionStarter objectionStarter = new ObjectionStarter();
        Objection objection = objectionStarter.execute(workspace, worker, workoutHistory, reason, objectionAlreadyOpenValidator);

        objectionRepository.save(objection);
        applicationEventPublisher.publishEvent(new ObjectionOpenEvent(workspace.getId(), objection.getId()));
    }

    public void voteToObjection(User loginedUser, Long workspaceId, Long objectionId, boolean willApprove) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspaceId);

        Objection objection = objectionRepository.findByIdOrThrow(objectionId);

        VoteManger voteManger = new VoteManger();
        Vote vote = voteManger.createVote(workspace, objection, worker, willApprove, voteValidator);

        voteRepository.save(vote);

        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspaceId);
        List<Vote> votes = voteRepository.findAllByObjectionId(objectionId);

        VoteReflector voteReflector = new VoteReflector(objection, votes, workers.size());
        voteReflector.apply();

    }


//    public void terminateExpiredObjection(User loginedUser, Long workspaceId) {
//        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
//        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspaceId);
//        List<Objection> expiredObjections = objectionRepository.getExpiredObjections(workspace.getId());
//        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
//
//        for (Objection expiredObjection : expiredObjections) {
//            VoteManger voteManger = new VoteManger(expiredObjection);
//            List<Vote> votes = voteManger.createAutoVote(workers);
//            voteRepository.saveAll(votes);
//            voteManger.applyAll(votes);
//            if (voteManger.closeIfOnMajorityOrDone(workers.size())) {
//                WorkoutHistory workoutHistory = workoutHistoryRepository.findByWorkoutConfirmationIdOrThrow(expiredObjection.getWorkoutConfirmation().getId());
//                rejectWorkoutHistory(voteManger, workoutHistory);
//            }
//        }
//    }


    public WorkspaceResultResponse getWorkspaceResult(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
        if (objectionRepository.existsByInProgress(workspace.getId())) {
            throw new InvalidStateException(ErrorCode.EXIST_OBJECTION_IN_PROGRESS);
        }

        if (workspace.isFullyCompleted()) {
            WorkspaceResult workspaceResult = workspaceResultRepository.getByWorkspaceId(workspace.getId());
            return getWorkspaceResultResponse(workspace, workers, workspaceResult);
        }

        WorkspaceDrawManger workspaceDrawManger = new WorkspaceDrawManger(workspace, workers);
        WorkspaceResult workspaceResult = workspaceDrawManger.draw();
        workspaceResult.apply();

        workspaceResultRepository.save(workspaceResult);
        return getWorkspaceResultResponse(workspace, workers, workspaceResult);
    }

    private WorkspaceResultResponse getWorkspaceResultResponse(Workspace workspace, List<Worker> workers, WorkspaceResult workspaceResult) {
        List<Worker> result = sortResult(workspaceResult.getWinner(), workspaceResult.getLoser(), workers);
        return new WorkspaceResultResponse(workspace.getTask(), result);
    }

    private List<Worker> sortResult(Worker winner, Worker loser, List<Worker> workers) {
        LinkedList<Worker> result = new LinkedList<>(workers);
        result.remove(winner);
        result.remove(loser);
        result.addFirst(winner);
        result.addLast(loser);
        return result;
    }
}
