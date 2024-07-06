package gymmi.service;

import gymmi.entity.Mission;
import gymmi.entity.Task;
import gymmi.entity.User;
import gymmi.entity.Worker;
import gymmi.entity.WorkingRecord;
import gymmi.entity.WorkingSummation;
import gymmi.entity.Workspace;
import gymmi.exception.AlreadyExistException;
import gymmi.exception.InvalidStateException;
import gymmi.exception.NotHavePermissionException;
import gymmi.exception.NotMatchedException;
import gymmi.repository.MissionRepository;
import gymmi.repository.TaskRepository;
import gymmi.repository.WorkerRepository;
import gymmi.repository.WorkingRecordRepository;
import gymmi.repository.WorkspaceRepository;
import gymmi.request.CreatingWorkspaceRequest;
import gymmi.request.EditingDescriptionOfWorkspaceRequest;
import gymmi.request.JoiningWorkspaceRequest;
import gymmi.request.MissionDTO;
import gymmi.request.WorkingMissionInWorkspaceRequest;
import gymmi.response.ContributedWorkingResponse;
import gymmi.response.InsideWorkspaceResponse;
import gymmi.response.JoinedWorkspaceResponse;
import gymmi.response.MatchingWorkspacePasswordResponse;
import gymmi.response.MissionResponse;
import gymmi.response.OpeningTasksBoxResponse;
import gymmi.response.WorkspaceIntroductionResponse;
import gymmi.response.WorkspaceResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final MissionRepository missionRepository;
    private final TaskRepository taskRepository;
    private final WorkingRecordRepository workingRecordRepository;

    @Transactional
    public Long createWorkspace(User loginedUser, CreatingWorkspaceRequest request) {
        if (workspaceRepository.findWorkspaceByByName(request.getName()).isPresent()) {
            throw new AlreadyExistException("이미 존재하는 워크스페이스 이름 입니다.");
        }

        Workspace workspace = Workspace.builder()
                .creator(loginedUser)
                .name(request.getName())
                .headCount(request.getHeadCount())
                .goalScore(request.getGoalScore())
                .description(request.getDescription())
                .tag(request.getTag())
                .build();
        Workspace savedWorkspace = workspaceRepository.save(workspace);

        List<MissionDTO> missionBoard = request.getMissionBoard();
        for (MissionDTO missionDTO : missionBoard) {
            Mission mission = Mission.builder()
                    .workspace(savedWorkspace)
                    .name(missionDTO.getMission())
                    .score(missionDTO.getScore())
                    .build();
            missionRepository.save(mission);
        }

        setTask(loginedUser, savedWorkspace, request.getTask());
        participateInWorkspace(loginedUser, savedWorkspace);

        return savedWorkspace.getId();
    }


    @Transactional
    public void joinWorkspace(User loginedUser, Long workspaceId, JoiningWorkspaceRequest request) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        if (!workspace.matchesPassword(request.getPassword())) {
            throw new NotMatchedException("비밀번호가 일치하지 않습니다.");
        }
        participateInWorkspace(loginedUser, workspace);
        setTask(loginedUser, workspace, request.getTask());
    }


    private void participateInWorkspace(User loginedUser, Workspace workspace) {
        if (workerRepository.findByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId()).isPresent()) {
            throw new AlreadyExistException("이미 참여한 워크스페이스 입니다.");
        }

        if (!workspace.isPreparing()) {
            throw new InvalidStateException("준비중인 워크스페이스에만 참여할 수 있습니다.");
        }

        int workerCount = workerRepository.countAllByWorkspaceId(workspace.getId());
        if (workspace.isFull(workerCount)) {
            throw new InvalidStateException("워크스페이스 인원이 가득 찼습니다.");
            // 동시에 참여하는 경우?? 검증로직 피할수도? 락사용
        }

        Worker worker = Worker.builder()
                .workspace(workspace)
                .user(loginedUser)
                .build();
        workerRepository.save(worker);
    }

    private void setTask(User loginedUser, Workspace workspace, String taskName) {
        if (taskRepository.findByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId()).isPresent()) {
            throw new AlreadyExistException("이미 테스크를 작성하였습니다.");
        }
        Task task = Task.builder()
                .workspace(workspace)
                .register(loginedUser)
                .name(taskName)
                .build();
        taskRepository.save(task);
    }

    public WorkspaceIntroductionResponse getWorkspaceIntroduction(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        return new WorkspaceIntroductionResponse(workspace);
    }

    private Worker validateIfWorkerIsInWorkspace(Long userId, Long workspaceId) {
        return workerRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotHavePermissionException("해당 워크스페이스의 참여자가 아니에요."));
    }

    public MatchingWorkspacePasswordResponse matchesWorkspacePassword(Long workspaceId, String workspacePassword) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        boolean matchingResult = workspace.matchesPassword(workspacePassword);
        return new MatchingWorkspacePasswordResponse(matchingResult);
    }

    public List<JoinedWorkspaceResponse> getJoinedWorkspaces(User loginedUser, int pageNumber) {
        List<Workspace> joinedWorkspaces = workspaceRepository.getJoinedWorkspacesByUserId(loginedUser.getId(),
                pageNumber);
        List<JoinedWorkspaceResponse> responses = new ArrayList<>();
        for (Workspace workspace : joinedWorkspaces) {
            int achievementScore = workspaceRepository.getAchievementScore(workspace.getId());
            JoinedWorkspaceResponse response = JoinedWorkspaceResponse.builder()
                    .id(workspace.getId())
                    .name(workspace.getName())
                    .creator(workspace.getCreator().getNickname())
                    .status(workspace.getStatus().name())
                    .tag(workspace.getTag())
                    .createdAt(workspace.getCreatedAt())
                    .goalScore(workspace.getGoalScore())
                    .achievementScore(achievementScore)
                    .build();
            responses.add(response);
        }
        return responses;
    }

    public List<WorkspaceResponse> getAllWorkspaces() {
        List<Workspace> workspaces = workspaceRepository.getAllWorkspaces();
        List<WorkspaceResponse> responses = new ArrayList<>();
        for (Workspace workspace : workspaces) {
            int achievementScore = workspaceRepository.getAchievementScore(workspace.getId());
            WorkspaceResponse response = WorkspaceResponse.builder()
                    .id(workspace.getId())
                    .name(workspace.getName())
                    .status(workspace.getStatus().name())
                    .goalScore(workspace.getGoalScore())
                    .createdAt(workspace.getCreatedAt())
                    .achievementScore(achievementScore)
                    .build();
            responses.add(response);
        }
        return responses;
    }

    @Transactional
    public void startWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        validateIfUserIsCreator(loginedUser, workspace);
        int workerCount = workerRepository.countAllByWorkspaceId(workspace.getId());
        if (workerCount < 2) {
            throw new InvalidStateException("최소 인원인 2명을 채워주세요.");
        }
        workspace.start();
    }

    @Transactional
    public void leaveWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        if (!workspace.isPreparing()) {
            throw new InvalidStateException("준비 단계에서만 나갈 수 있습니다.");
        }

        if (workspace.isCreatedBy(loginedUser)) {
            validateIfAnyWorkerExistsInWorkspaceExcludeCreator(workspace);
            deleteTaskAndWorker(loginedUser, workspaceId);
            deleteMissionsAndWorkspace(workspaceId, workspace);
            return;
        }

        deleteTaskAndWorker(loginedUser, workspaceId);
    }

    private void validateIfAnyWorkerExistsInWorkspaceExcludeCreator(Workspace workspace) {
        int workerCount = workerRepository.countAllByWorkspaceId(workspace.getId());
        if (workerCount != 1) {
            throw new InvalidStateException("방장 이외에 참여자가 존재합니다.");
        }
    }

    private void deleteMissionsAndWorkspace(Long workspaceId, Workspace workspace) {
        missionRepository.deleteAllByWorkspaceId(workspace.getId());
        workspaceRepository.deleteById(workspaceId);
    }

    private void deleteTaskAndWorker(User loginedUser, Long workspaceId) {
        taskRepository.deleteByUserIdAndWorkspaceId(loginedUser.getId(), workspaceId);
        workerRepository.deleteByUserIdAndWorkspaceId(loginedUser.getId(), workspaceId);
    }

    public InsideWorkspaceResponse enterWorkspace(User logiendUser, Long workspaceId) {
        validateIfWorkerIsInWorkspace(logiendUser.getId(), workspaceId);

        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        List<Worker> sortedWorkers = workerRepository.getAllByWorkspaceIdOrderByContributedScore(workspaceId);

        List<Integer> workerRanks = rankTied(sortedWorkers);
        int achievementScore = workspaceRepository.getAchievementScore(workspaceId);

        return InsideWorkspaceResponse.builder()
                .workspace(workspace)
                .achievementScore(achievementScore)
                .sortedWorkers(sortedWorkers)
                .workerRanks(workerRanks)
                .loginedUser(logiendUser)
                .build();
    }

    private List<Integer> rankTied(List<Worker> sortedWorker) {
        Queue<Integer> rankBuffer = new LinkedList<>();
        List<Integer> workerRanks = new ArrayList<>();

        Integer previousScore = sortedWorker.get(0).getContributedScore();
        for (int i = 0; i < sortedWorker.size(); i++) {
            rankBuffer.add(i + 1);
            Integer workerScore = sortedWorker.get(i).getContributedScore();
            if (workerScore == previousScore) {
                workerRanks.add(rankBuffer.element());
                continue;
            }
            previousScore = workerScore;
            rankBuffer.remove();
            workerRanks.add(rankBuffer.element());
        }
        return workerRanks;
    }

    public List<MissionResponse> getMissionsInWorkspace(User loginedUser, Long workspaceId) {
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        List<Mission> missions = missionRepository.getAllByWorkspaceId(workspaceId);
        return missions.stream()
                .map(MissionResponse::new)
                .toList();
    }

    @Transactional // lock 사용?
    public Integer workMissionsInWorkspace(
            User loginedUser,
            Long workspaceId,
            List<WorkingMissionInWorkspaceRequest> requests
    ) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        if (!workspace.isInProgress()) {
            throw new InvalidStateException("워크스페이스가 시작중이 아니에요.");
        }

        int workingScore = 0;
        for (WorkingMissionInWorkspaceRequest request : requests) {
            Mission mission = missionRepository.getByMissionId(request.getId());
            WorkingRecord workingRecord = worker.doMission(mission, request.getCount());
            workingRecordRepository.save(workingRecord);
            workingScore += workingRecord.getContributedScore();
        }

        worker.addWorkingScore(workingScore);

        int achievementScore = workspaceRepository.getAchievementScore(workspaceId);

        if (workspace.achieves(achievementScore)) {
            workspace.complete();
            drawTask(workspaceId);
        }

        return workingScore;
    }

    private void drawTask(Long workspaceId) {
        List<Task> tasks = taskRepository.getAllByWorkspaceId(workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceIdOrderByContributedScore(workspaceId);
        TaskDraw taskDraw = new TaskDraw(tasks);
        Task task = taskDraw.pickOneAmong(workers, rankTied(workers));
        task.changeToPicked();
    }

    public List<ContributedWorkingResponse> getContributedWorkingsOfWorkerInWorkspace(
            User loginedUser,
            Long workspaceId,
            Long userId
    ) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        Worker targetWorker = validateIfWorkerIsInWorkspace(userId, workspaceId);

        List<Mission> missions = missionRepository.getAllByWorkspaceId(workspace.getId());

        List<WorkingRecord> workingRecords = workingRecordRepository.getAllByWorkerId(targetWorker.getId());
        WorkingSummation workingSummation = new WorkingSummation(workingRecords);

        return missions.stream()
                .map(m -> new ContributedWorkingResponse(m, workingSummation))
                .toList();
    }

    @Transactional
    public OpeningTasksBoxResponse openTaskBoxInWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        if (!workspace.isCompleted()) {
            throw new InvalidStateException("목표점수를 달성해주세요!");
        }
        List<Task> tasks = taskRepository.getAllByWorkspaceId(workspaceId);
        return new OpeningTasksBoxResponse(tasks);
    }

    @Transactional
    public void editDescription(
            User loginedUser,
            Long workspaceId,
            EditingDescriptionOfWorkspaceRequest request
    ) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfUserIsCreator(loginedUser, workspace);
        workspace.editDescription(request.getDescription());
    }

    private void validateIfUserIsCreator(User loginedUser, Workspace workspace) {
        if (!workspace.isCreatedBy(loginedUser)) {
            throw new NotHavePermissionException("방장이 아닙니다.");
        }
    }
}

