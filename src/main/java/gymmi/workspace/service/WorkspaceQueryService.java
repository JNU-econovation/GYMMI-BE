package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.exception.class1.NotHavePermissionException;
import gymmi.exception.message.ErrorCode;
import gymmi.workspace.domain.Mission;
import gymmi.workspace.domain.Worker;
import gymmi.workspace.domain.WorkoutRecord;
import gymmi.workspace.domain.WorkoutSummation;
import gymmi.workspace.domain.Workspace;
import gymmi.workspace.domain.WorkspaceGateChecker;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.repository.MissionRepository;
import gymmi.workspace.repository.WorkedRepository;
import gymmi.workspace.repository.WorkerRepository;
import gymmi.workspace.repository.WorkoutRecordRepository;
import gymmi.workspace.repository.WorkspaceRepository;
import gymmi.workspace.response.CheckingCreationOfWorkspaceResponse;
import gymmi.workspace.response.CheckingEntranceOfWorkspaceResponse;
import gymmi.workspace.response.ContributedWorkingResponse;
import gymmi.workspace.response.InsideWorkspaceResponse;
import gymmi.workspace.response.JoinedWorkspaceResponse;
import gymmi.workspace.response.MatchingWorkspacePasswordResponse;
import gymmi.workspace.response.MissionResponse;
import gymmi.workspace.response.WorkspaceIntroductionResponse;
import gymmi.workspace.response.WorkspaceResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final MissionRepository missionRepository;
    private final WorkoutRecordRepository workoutRecordRepository;
    private final WorkedRepository workedRepository;

    public WorkspaceIntroductionResponse getWorkspaceIntroduction(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);

        return new WorkspaceIntroductionResponse(workspace, workspace.isCreatedBy(loginedUser));
    }

    private Worker validateIfWorkerIsInWorkspace(Long userId, Long workspaceId) {
        return workerRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotHavePermissionException(ErrorCode.NOT_JOINED_WORKSPACE));
    }

    public List<JoinedWorkspaceResponse> getJoinedAllWorkspaces(User loginedUser, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE);
        List<Workspace> workspaces = workspaceRepository.getJoinedWorkspacesByUserIdOrderBy_(loginedUser.getId(),
                pageable);
        Map<Workspace, Integer> achievementScores = workspaceRepository.getAchievementScoresIn(workspaces);

        return workspaces.stream()
                .map(workspace -> new JoinedWorkspaceResponse(workspace, achievementScores.get(workspace)))
                .toList();
    }

    public List<WorkspaceResponse> getAllWorkspaces(WorkspaceStatus status, String keyword, int pageNumber) {
        List<Workspace> workspaces = workspaceRepository.getAllWorkspaces(status, keyword,
                PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE));
        Map<Workspace, Integer> achievementScores = workspaceRepository.getAchievementScoresIn(workspaces);
        return workspaces.stream()
                .map(workspace -> new WorkspaceResponse(workspace, achievementScores.get(workspace)))
                .toList();
    }

    public MatchingWorkspacePasswordResponse matchesWorkspacePassword(Long workspaceId, String workspacePassword) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);

        boolean matchingResult = workspace.matchesPassword(workspacePassword);
        return new MatchingWorkspacePasswordResponse(matchingResult);
    }

    public List<MissionResponse> getMissionsInWorkspace(User loginedUser, Long workspaceId) {
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        List<Mission> missions = missionRepository.getAllByWorkspaceId(workspaceId);
        return missions.stream()
                .map(MissionResponse::new)
                .toList();
    }

    public List<ContributedWorkingResponse> getContributedWorkoutOfWorkerInWorkspace(
            User loginedUser,
            Long workspaceId,
            Long userId
    ) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        Worker targetWorker = validateIfWorkerIsInWorkspace(userId, workspaceId);
        List<Mission> missions = missionRepository.getAllByWorkspaceId(workspace.getId());
        List<WorkoutRecord> workoutRecords = workoutRecordRepository.getAllByWorkerId(targetWorker.getId());

        WorkoutSummation workoutSummation = new WorkoutSummation(workoutRecords);
        return missions.stream()
                .map(m -> new ContributedWorkingResponse(m, workoutSummation))
                .toList();
    }

    public CheckingEntranceOfWorkspaceResponse checkEnteringWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
        Worker worker = workerRepository.findByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId())
                .orElseThrow(null);

        WorkspaceGateChecker workspaceGateChecker = new WorkspaceGateChecker(workspace, workers);
        boolean isFull = workspaceGateChecker.canJoin();
        boolean isExist = workspaceGateChecker.canEnter(worker);
        return new CheckingEntranceOfWorkspaceResponse(isExist, isFull);
    }

    public CheckingCreationOfWorkspaceResponse checkCreatingOfWorkspace(User loginedUser) {
        long countOfJoinedWorkspaces =
                workspaceRepository.getCountsOfJoinedWorkspacesExcludeCompleted(loginedUser.getId());
        boolean canCreate = countOfJoinedWorkspaces < 5;
        return new CheckingCreationOfWorkspaceResponse(canCreate);
    }

    public InsideWorkspaceResponse enterWorkspace(User logiendUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspaceId);
        validateIfWorkerIsInWorkspace(logiendUser.getId(), workspaceId);
        int achievementScore = workspaceRepository.getAchievementScore(workspaceId);

        return new InsideWorkspaceResponse(workspace, workers, achievementScore, logiendUser);
    }

}
