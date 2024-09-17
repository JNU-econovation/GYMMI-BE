package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.exception.class1.AlreadyExistException;
import gymmi.exception.class1.InvalidStateException;
import gymmi.exception.message.ErrorCode;
import gymmi.workspace.domain.Mission;
import gymmi.workspace.domain.Task;
import gymmi.workspace.domain.Worked;
import gymmi.workspace.domain.Worker;
import gymmi.workspace.domain.WorkerLeavedEvent;
import gymmi.workspace.domain.Workspace;
import gymmi.workspace.domain.WorkspaceDrawManager;
import gymmi.workspace.domain.WorkspaceEditManager;
import gymmi.workspace.domain.WorkspaceInitializer;
import gymmi.workspace.domain.WorkspacePreparingManager;
import gymmi.workspace.domain.WorkspaceProgressManager;
import gymmi.workspace.repository.MissionRepository;
import gymmi.workspace.repository.WorkedRepository;
import gymmi.workspace.repository.WorkerRepository;
import gymmi.workspace.repository.WorkspaceRepository;
import gymmi.workspace.request.CreatingWorkspaceRequest;
import gymmi.workspace.request.EditingIntroductionOfWorkspaceRequest;
import gymmi.workspace.request.JoiningWorkspaceRequest;
import gymmi.workspace.request.WorkingMissionInWorkspaceRequest;
import gymmi.workspace.response.OpeningTasksBoxResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceCommandService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final MissionRepository missionRepository;
    private final WorkedRepository workedRepository;

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

        workerRepository.deleteById(workerLeavedEvent.getWorker().getId());
        if (workerLeavedEvent.isLastOne()) {
            missionRepository.deleteAllByWorkspaceId(workspace.getId());
            workspaceRepository.deleteById(workspaceId);
        }
    }

    @Transactional // 동시성 문제
    public Integer workMissionsInWorkspace(
            User loginedUser,
            Long workspaceId,
            List<WorkingMissionInWorkspaceRequest> requests
    ) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = workerRepository.getByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId());
        List<Mission> missions = missionRepository.getAllByWorkspaceId(workspace.getId());
        Map<Mission, Integer> workouts = getWorkouts(requests);

        WorkspaceProgressManager workspaceProgressManager = new WorkspaceProgressManager(workspace, missions);
        Worked worked = workspaceProgressManager.doWorkout(worker, workouts);
        worked.apply();

        workedRepository.save(worked);
        int achievementScore = workspaceRepository.getAchievementScore(workspaceId);

        workspaceProgressManager.completeWhenGoalScoreIsAchieved(achievementScore);
        return worked.getSum();
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

}
