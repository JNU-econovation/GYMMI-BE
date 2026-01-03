package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.eventlistener.event.*;
import gymmi.exceptionhandler.exception.AlreadyExistException;
import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.service.ImageUse;
import gymmi.workspace.domain.ObjectionManager;
import gymmi.workspace.domain.WorkspaceDrawManger;
import gymmi.workspace.domain.WorkspaceEditManager;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.repository.*;
import gymmi.workspace.request.EditingIntroductionOfWorkspaceRequest;
import gymmi.workspace.request.ObjectionRequest;
import gymmi.workspace.request.VoteRequest;
import gymmi.workspace.request.WorkoutRequest;
import gymmi.workspace.response.WorkspaceResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkspaceCommandService {

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
    private final WorkspacePhaseChangeEventPublisher workspacePhaseChangeEventPublisher;

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
        List<WorkoutRecord> workoutRecords = workoutRequestMapper.createWorkoutRecords(workspace.getId(), workoutHistory, workoutRequest.getMissions());
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

    @Transactional
    public void editIntroduction(
            User loginedUser,
            Long workspaceId,
            EditingIntroductionOfWorkspaceRequest request
    ) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspace.getId());

        WorkspaceEditManager workspaceEditManager = new WorkspaceEditManager(workspace, worker);
        workspaceEditManager.edit(request.getDescription(), request.getTag(), request.getTask());
    }

    public void toggleRegistrationOfFavoriteMission(User loginedUser, Long workspaceId, Long missionId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        Mission mission = missionRepository.findByIdOrThrow(missionId);

        mission.canBeReadIn(workspace);

        Optional<FavoriteMission> favoriteMission =
                favoriteMissionRepository.findByWorkerIdAndMissionId(worker.getId(), missionId);
        favoriteMission.ifPresentOrElse(
                fm -> favoriteMissionRepository.deleteById(fm.getId()),
                () -> favoriteMissionRepository.save(new FavoriteMission(worker, mission))
        );
    }

    private Worker validateIfWorkerIsInWorkspace(Long userId, Long workspaceId) {
        return workerRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotHavePermissionException(ErrorCode.NOT_JOINED_WORKSPACE));
    }

    public void objectToWorkoutConfirmation(User loginedUser, Long workspaceId, Long workoutConfirmationId, ObjectionRequest request) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutConfirmationId(workoutConfirmationId);
//        workoutHistory.canBeReadIn(workspace);
        if (objectionRepository.findByWorkoutConfirmationId(workoutConfirmationId).isPresent()) {
            throw new AlreadyExistException(ErrorCode.ALREADY_OBJECTED);
        }
        if (!workspace.isInProgress()) {
            throw new InvalidStateException(ErrorCode.INACTIVE_WORKSPACE);
        }
        Objection objection = Objection.builder()
                .subject(worker)
                .reason(request.getReason())
                .workoutConfirmation(workoutHistory.getWorkoutConfirmation())
                .build();
        objectionRepository.save(objection);

        applicationEventPublisher.publishEvent(new ObjectionOpenEvent(workspace.getId(), objection.getId()));
        //리펙터링
    }

    public void voteToObjection(User loginedUser, Long workspaceId, Long objectionId, VoteRequest request) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        Objection objection = objectionRepository.getByObjectionId(objectionId);
        objection.canBeReadIn(workspace);

        ObjectionManager objectionManager = new ObjectionManager(objection);
        Vote vote = objectionManager.createVote(worker, request.getWillApprove());

        voteRepository.save(vote);
        objectionManager.apply(vote);

        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspaceId);

        if (objectionManager.closeIfOnMajorityOrDone(workers.size())) {
            WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutConfirmationId(objection.getWorkoutConfirmation().getId());
            rejectWorkoutHistory(objectionManager, workoutHistory);
        }
    }

    private void rejectWorkoutHistory(ObjectionManager objectionManager, WorkoutHistory workoutHistory) {
        if (objectionManager.isApproved()) {
            workoutHistory.cancel();
        }
    }

    public void terminateExpiredObjection(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        List<Objection> expiredObjections = objectionRepository.getExpiredObjections(workspace.getId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());

        for (Objection expiredObjection : expiredObjections) {
            ObjectionManager objectionManager = new ObjectionManager(expiredObjection);
            List<Vote> votes = objectionManager.createAutoVote(workers);
            voteRepository.saveAll(votes);
            objectionManager.applyAll(votes);
            if (objectionManager.closeIfOnMajorityOrDone(workers.size())) {
                WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutConfirmationId(expiredObjection.getWorkoutConfirmation().getId());
                rejectWorkoutHistory(objectionManager, workoutHistory);
            }
        }
    }

    public WorkspaceResultResponse getWorkspaceResult(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
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
