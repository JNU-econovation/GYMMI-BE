package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.exceptionhandler.exception.AlreadyExistException;
import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.*;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.repository.*;
import gymmi.workspace.request.*;
import gymmi.workspace.response.OpeningTasksBoxResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkspaceCommandService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final MissionRepository missionRepository;
    private final WorkoutHistoryRepository workoutHistoryRepository;
    private final FavoriteMissionRepository favoriteMissionRepository;
    private final TackleRepository tackleRepository;
    private final VoteRepository voteRepository;

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
        Worker worker = workspacePreparingManager.allow(loginedUser, request.getPassword(), request.getTask());

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

        workerRepository.delete(workerLeavedEvent.getWorker());
        if (workerLeavedEvent.isLastOne()) {
            missionRepository.deleteAllByWorkspaceId(workspace.getId());
            workspaceRepository.deleteById(workspaceId);
        }
    }

    @Transactional // 동시성 문제
    public Integer workMissionsInWorkspace(
            User loginedUser,
            Long workspaceId,
            WorkoutRequest workoutRequest
    ) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = workerRepository.getByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId());
        List<Mission> missions = missionRepository.getAllByWorkspaceId(workspace.getId());
        Map<Mission, Integer> workouts = getWorkouts(workoutRequest.getMissions());

        WorkspaceProgressManager workspaceProgressManager = new WorkspaceProgressManager(workspace, missions);

        WorkoutHistory workoutHistory = workspaceProgressManager.doWorkout(
                worker,
                workouts,
                new WorkoutProof(workoutRequest.getImageUrl(), workoutRequest.getComment())
        );
        workoutHistory.apply();

        workoutHistoryRepository.save(workoutHistory);
        int achievementScore = workspaceRepository.getAchievementScore(workspaceId);

        workspaceProgressManager.completeWhenGoalScoreIsAchieved(achievementScore);
        return workoutHistory.getSum();
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
    public OpeningTasksBoxResponse openTaskBoxInWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = workerRepository.getByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());

        WorkspaceDrawManager workspaceDrawManager = new WorkspaceDrawManager(workspace, workers);
        workspaceDrawManager.drawIfNotPicked();
        List<Task> tasks = workspaceDrawManager.getTasks(worker);
        return new OpeningTasksBoxResponse(tasks);
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

    public void tackleToWorkoutConfirmation(User loginedUser, Long workspaceId, Long workoutConfirmationId, TackleRequest request) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutProofId(workoutConfirmationId);
        workoutHistory.canBeReadIn(workspace);
        if (tackleRepository.findByWorkoutConfirmationId(workoutConfirmationId).isPresent()) {
            throw new AlreadyExistException(ErrorCode.ALREADY_TACKLED);
        }
        Tackle tackle = Tackle.builder()
                .subject(worker)
                .reason(request.getReason())
                .workoutProof(workoutHistory.getWorkoutProof())
                .build();
        tackleRepository.save(tackle);
    }

    public void voteToTackle(User loginedUser, Long workspaceId, Long tackleId, VoteRequest request) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        Tackle tackle = tackleRepository.getByTackleId(tackleId);
        tackle.canBeReadIn(workspace);

        TackleManager voteManager = new TackleManager(tackle);
        Vote vote = voteManager.createVote(worker, request.getIsAgree());
        voteRepository.save(vote);

        voteManager.closeIfOnMajorityOrDone(workspace.getHeadCount());
    }
}
