package gymmi.service;

import gymmi.entity.*;
import gymmi.exception.AlreadyExistException;
import gymmi.exception.InvalidStateException;
import gymmi.exception.NotHavePermissionException;
import gymmi.repository.*;
import gymmi.request.CreatingWorkspaceRequest;
import gymmi.request.EditingIntroductionOfWorkspaceRequest;
import gymmi.request.JoiningWorkspaceRequest;
import gymmi.request.WorkingMissionInWorkspaceRequest;
import gymmi.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final MissionRepository missionRepository;
    private final TaskRepository taskRepository;
    private final WorkingRecordRepository workingRecordRepository;

    @Transactional
    // 중복 요청
    public Long createWorkspace(User loginedUser, CreatingWorkspaceRequest request) {
        validateCountOfWorkspaces(loginedUser.getId());
        if (workspaceRepository.existsByName(request.getName())) {
            throw new AlreadyExistException("이미 존재하는 워크스페이스 이름 입니다.");
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

        WorkspaceManager workspaceManager = new WorkspaceManager(workspace, workers);
        Worker worker = workspaceManager.allow(loginedUser, request.getPassword(), request.getTask());

        workerRepository.save(worker);
    }

    private void validateCountOfWorkspaces(Long userId) {
        long countOfJoinedWorkspaces =
                workspaceRepository.getCountsOfJoinedWorkspacesExcludeCompleted(userId);
        if (countOfJoinedWorkspaces >= 5) {
            throw new InvalidStateException("워크스페이스는 5개까지 참여 가능합니다.(완료된 워크스페이스 제외)");
        }
    }

    public WorkspaceIntroductionResponse getWorkspaceIntroduction(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);

        return new WorkspaceIntroductionResponse(workspace, workspace.isCreatedBy(loginedUser));
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

    public List<JoinedWorkspaceResponse> getJoinedAllWorkspaces(User loginedUser, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE);
        List<Workspace> workspaces = workspaceRepository.getJoinedWorkspacesByUserIdOrderBy_(loginedUser.getId(), pageable);
        Map<Workspace, Integer> achievementScores = workspaceRepository.getAchievementScoresIn(workspaces);

        return workspaces.stream()
                .map(workspace -> new JoinedWorkspaceResponse(workspace, achievementScores.get(workspace)))
                .toList();
    }

    public List<WorkspaceResponse> getAllWorkspaces(WorkspaceStatus status, String keyword, int pageNumber) {
        List<Workspace> workspaces = workspaceRepository.getAllWorkspaces(status, keyword, PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE));
        Map<Workspace, Integer> achievementScores = workspaceRepository.getAchievementScoresIn(workspaces);
        return workspaces.stream()
                .map(workspace -> new WorkspaceResponse(workspace, achievementScores.get(workspace)))
                .toList();
    }

    @Transactional
    public void startWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        validateIfUserIsCreator(loginedUser, workspace);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());

        WorkspaceManager workspaceManager = new WorkspaceManager(workspace, workers);
        workspaceManager.start();
    }

    @Transactional
    public void leaveWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());

        WorkspaceManager workspaceManager = new WorkspaceManager(workspace, workers);
        WorkerLeavedEvent workerLeavedEvent = workspaceManager.release(worker);

        workerRepository.deleteById(workerLeavedEvent.getWorker().getId());
        if (workerLeavedEvent.isLastOne()) {
            missionRepository.deleteAllByWorkspaceId(workspace.getId());
            workspaceRepository.deleteById(workspaceId);
        }
    }

    // TODO: 수정필요
    public InsideWorkspaceResponse enterWorkspace(User logiendUser, Long workspaceId) {
        Worker worker = validateIfWorkerIsInWorkspace(logiendUser.getId(), workspaceId);

        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        List<Worker> sortedWorkers = workerRepository.getAllByWorkspaceId(workspaceId);
        List<Integer> workerRanks = rankTied(sortedWorkers);

        int achievementScore = workspaceRepository.getAchievementScore(workspaceId);

        List<Worker> workers = sortedWorkers.stream()
                .filter(w -> (!w.equals(worker)))
                .collect(Collectors.toList());
        workers.add(0, worker);

        // 랭크 로직은 수정해야할듯.
        return InsideWorkspaceResponse.builder()
                .workspace(workspace)
                .achievementScore(achievementScore)
                .sortedWorkers(workers)
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

    // todo 시작하기
    @Transactional // 동시성 문제
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

    @Transactional // 동시성 문제
    public Integer workMissionsInWorkspace1(
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
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspaceId);
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
    public void editIntroduction(
            User loginedUser,
            Long workspaceId,
            EditingIntroductionOfWorkspaceRequest request
    ) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfUserIsCreator(loginedUser, workspace);
        workspace.editDescription(request.getDescription());
        workspace.editTag(request.getTag());
    }

    private void validateIfUserIsCreator(User loginedUser, Workspace workspace) {
        if (!workspace.isCreatedBy(loginedUser)) {
            throw new NotHavePermissionException("방장이 아닙니다.");
        }
    }

    public CheckingEntranceOfWorkspaceResponse checkEnteringWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        boolean isWorker = workerRepository.existsByUserIdAndWorkspaceId(loginedUser.getId(), workspaceId);

        Integer count = workerRepository.countAllByWorkspaceId(workspaceId);
        boolean isFull = workspace.isFull(count);
        return new CheckingEntranceOfWorkspaceResponse(isWorker, isFull);
    }

    public CheckingCreationOfWorkspaceResponse checkCreatingOfWorkspace(User loginedUser) {
        long countOfJoinedWorkspaces =
                workspaceRepository.getCountsOfJoinedWorkspacesExcludeCompleted(loginedUser.getId());
        boolean canCreate = countOfJoinedWorkspaces < 5;
        return new CheckingCreationOfWorkspaceResponse(canCreate);
    }
}
