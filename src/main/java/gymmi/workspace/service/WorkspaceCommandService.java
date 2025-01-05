package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.exceptionhandler.exception.AlreadyExistException;
import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.photoboard.domain.entity.PhotoFeedImage;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.photoboard.service.PhotoFeedService;
import gymmi.service.S3Service;
import gymmi.workspace.domain.*;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.repository.*;
import gymmi.workspace.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final S3Service s3Service;
    private final PhotoFeedService photoFeedService;

    @Transactional
    // 중복 요청
    public Long createWorkspace(User loginedUser, CreatingWorkspaceRequest request) {
        validateCountOfWorkspaces(loginedUser.getId());
        if (workspaceRepository.existsByName(request.getName())) {
            throw new AlreadyExistException(ErrorCode.ALREADY_USED_WORKSPACE_NAME);
        }

        WorkspaceInitializer workspaceInitializer = new WorkspaceInitializer();
        workspaceInitializer.init(loginedUser, request);

        Workspace workspace = workspaceRepository.save(workspaceInitializer.getWorkspace());
        missionRepository.saveAll(workspaceInitializer.getMissions());
        workerRepository.save(workspaceInitializer.getWorker());

        return workspace.getId();
    }

    @Transactional
    // 동시 참여 -> 인원수 초과, 중복 요청 -> 중복 참여자 존재
    public void joinWorkspace(User loginedUser, Long workspaceId, JoiningWorkspaceRequest request) {
        validateCountOfWorkspaces(loginedUser.getId());
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());

        WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);
        Worker worker = workspacePreparingManager.allow(loginedUser, request.getPassword());

        workerRepository.save(worker);
    }

    private void validateCountOfWorkspaces(Long userId) {
        long countOfJoinedWorkspaces =
                workspaceRepository.getCountsOfJoinedWorkspacesExcludeCompleted(userId);
        if (countOfJoinedWorkspaces >= 5) {
            throw new InvalidStateException(ErrorCode.EXCEED_MAX_JOINED_WORKSPACE);
        }
    }

    @Transactional
    public void startWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = workerRepository.getByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());

        WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);
        workspacePreparingManager.startBy(worker);
    }

    @Transactional
    public void leaveWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = workerRepository.getByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());

        WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);
        WorkerLeavedEvent workerLeavedEvent = workspacePreparingManager.release(worker);

        favoriteMissionRepository.deleteAllByWorkerId(workerLeavedEvent.getWorker().getId());
        workerRepository.delete(workerLeavedEvent.getWorker());
        if (workerLeavedEvent.isLastOne()) {
            missionRepository.deleteAllByWorkspaceId(workspace.getId());
            workspaceRepository.deleteById(workspaceId);
        }
    }

    @Transactional // 동시성 문제, 이벤트 사용하면 좋을듯
    public Integer workMissionsInWorkspace(
            User loginedUser,
            Long workspaceId,
            WorkoutRequest workoutRequest
    ) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = workerRepository.getByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId());
        List<Mission> missions = missionRepository.getAllByWorkspaceId(workspace.getId());
        Map<Mission, Integer> workouts = getWorkouts(workoutRequest.getMissions());
        validateDailyWorkoutHistoryCount(worker.getId());

        WorkspaceProgressManager workspaceProgressManager = new WorkspaceProgressManager(workspace, missions);
        WorkoutHistory workoutHistory = workspaceProgressManager.doWorkout(
                worker,
                workouts,
                new WorkoutConfirmation(workoutRequest.getImageUrl(), workoutRequest.getComment())
        );
        workoutHistory.apply();

        s3Service.checkObjectExist(WorkoutConfirmation.IMAGE_USE, workoutRequest.getImageUrl());
        workoutHistoryRepository.save(workoutHistory);
        int achievementScore = workspaceRepository.getAchievementScore(workspaceId);

        workspaceProgressManager.completeWhenGoalScoreIsAchieved(achievementScore);
        linkToPhotoBoardIfRequested(loginedUser, workoutRequest);
        return workoutHistory.getSum();
    }

    private void linkToPhotoBoardIfRequested(User loginedUser, WorkoutRequest workoutRequest) {
        if (workoutRequest.getWillLink()) {
            String filename = s3Service.copy(WorkoutConfirmation.IMAGE_USE, workoutRequest.getImageUrl(), PhotoFeedImage.IMAGE_USE);
            photoFeedService.createPhotoFeed(loginedUser, new CreatePhotoFeedRequest(filename, workoutRequest.getComment()));
        }
    }

    private void validateDailyWorkoutHistoryCount(Long workerId) {
        List<WorkoutHistory> workoutHistories = workoutHistoryRepository.getAllByDate(workerId, LocalDate.now());
        if (workoutHistories.size() >= 3) {
            throw new InvalidStateException(ErrorCode.EXCEED_MAX_DAILY_WORKOUT_HISTORY_COUNT);
        }
    }

    private Map<Mission, Integer> getWorkouts(List<WorkingMissionInWorkspaceRequest> requests) {
        Map<Mission, Integer> workouts = new HashMap<>();
        for (WorkingMissionInWorkspaceRequest request : requests) {
            Mission mission = missionRepository.getByMissionId(request.getId());
            workouts.put(mission, request.getCount());
        }
        return workouts;
    }

    @Transactional
    public void editIntroduction(
            User loginedUser,
            Long workspaceId,
            EditingIntroductionOfWorkspaceRequest request
    ) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = workerRepository.getByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId());

        WorkspaceEditManager workspaceEditManager = new WorkspaceEditManager(workspace, worker);
        workspaceEditManager.edit(request.getDescription(), request.getTag());
    }

    public void toggleRegistrationOfFavoriteMission(User loginedUser, Long workspaceId, Long missionId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        Mission mission = missionRepository.getByMissionId(missionId);

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
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutConfirmationId(workoutConfirmationId);
        workoutHistory.canBeReadIn(workspace);
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
        //리펙터링
    }

    public void voteToObjection(User loginedUser, Long workspaceId, Long objectionId, VoteRequest request) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        Objection objection = objectionRepository.getByObjectionId(objectionId);
        objection.canBeReadIn(workspace);

        ObjectionManager objectionManager = new ObjectionManager(objection);
        Vote vote = objectionManager.createVote(worker, request.getWillApprove());
        voteRepository.save(vote);

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
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        List<Objection> expiredObjections = objectionRepository.getExpiredObjections(workspace.getId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());

        for (Objection expiredObjection : expiredObjections) {
            ObjectionManager objectionManager = new ObjectionManager(expiredObjection);
            List<Vote> votes = objectionManager.createAutoVote(workers);
            voteRepository.saveAll(votes);
            if (objectionManager.closeIfOnMajorityOrDone(workers.size())) {
                WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutConfirmationId(expiredObjection.getWorkoutConfirmation().getId());
                rejectWorkoutHistory(objectionManager, workoutHistory);
            }
        }
    }


}
