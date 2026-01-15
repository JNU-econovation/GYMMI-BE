package gymmi.workspace.objection.service;

import gymmi.user.domain.User;
import gymmi.global.eventlistener.event.ObjectionOpenEvent;
import gymmi.global.exception.exceptiontype.NotHavePermissionException;
import gymmi.global.exception.message.ErrorCode;
import gymmi.workspace.objection.domain.*;
import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.objection.repository.ObjectionRepository;
import gymmi.workspace.objection.controller.response.ObjectionAlarmResponse;
import gymmi.workspace.objection.controller.response.ObjectionResponse;
import gymmi.workspace.vote.domain.entity.Vote;
import gymmi.workspace.vote.repository.VoteRepository;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.workspace.repository.WorkerRepository;
import gymmi.workspace.workout.repository.WorkoutHistoryRepository;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectionService {

    private final WorkoutHistoryRepository workoutHistoryRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final ObjectionRepository objectionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VoteRepository voteRepository;


    public void objectToWorkoutHistory(User loginedUser, Long workspaceId, Long workoutConfirmationId, String reason) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspaceId);
        WorkoutHistory workoutHistory = workoutHistoryRepository.findByWorkoutConfirmationIdOrThrow(workoutConfirmationId);

        boolean isPresent = objectionRepository.findByWorkoutHistoryId(workoutHistory.getId()).isPresent();
        ObjectionAlreadyOpenValidator.validate(isPresent);

        ObjectionStarter objectionStarter = new ObjectionStarter();
        Objection objection = objectionStarter.execute(workspace, worker, workoutHistory, reason);

        objectionRepository.save(objection);
        applicationEventPublisher.publishEvent(new ObjectionOpenEvent(workspace.getId(), objection.getId()));
    }

    public List<ObjectionAlarmResponse> getObjections(User loginedUser, Long workspaceId, int pageNumber, ObjectionStatus objectionStatus) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        PageRequest pageRequest = PageRequest.of(pageNumber, 10);
        List<Objection> objections = objectionRepository.getAllBy(workspaceId, worker.getId(), objectionStatus, pageRequest);
        return generateObjectionAlarmResponse(worker, objections);
    }

    private List<ObjectionAlarmResponse> generateObjectionAlarmResponse(Worker worker, List<Objection> objections) {
        List<ObjectionAlarmResponse> responses = new ArrayList<>();
        for (Objection objection : objections) {
            WorkoutHistory workoutHistory = workoutHistoryRepository.findByWorkoutConfirmationIdOrThrow(objection.getWorkoutHistory().getWorkoutConfirmation().getId());
            boolean voteCompletion = objection.hasVoteBy(worker);
            responses.add(new ObjectionAlarmResponse(objection, workoutHistory.getWorker().getNickname(), voteCompletion));
        }
        return responses;
    }

    private Worker validateIfWorkerIsInWorkspace(Long userId, Long workspaceId) {
        return workerRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotHavePermissionException(ErrorCode.NOT_JOINED_WORKSPACE));
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

    public ObjectionResponse getObjection(User loginedUser, Long workspaceId, Long objectionId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspace.getId());
        Objection objection = objectionRepository.findByIdOrThrow(objectionId);

        ObjectionInWorkspaceValidator.validate(workspace, objection);

        List<Vote> votes = voteRepository.findAllByObjectionId(objection.getId());
        WorkoutHistory workoutHistory = workoutHistoryRepository.findByWorkoutConfirmationIdOrThrow(objection.getWorkoutHistory().getWorkoutConfirmation().getId());

        ObjectionResponseGenerator objectionResponseGenerator = new ObjectionResponseGenerator(workspace, worker, objection, votes, workoutHistory);

        return objectionResponseGenerator.generate();
    }
}
