package gymmi.workspace.workspace.service;

import gymmi.etc.domain.entity.User;
import gymmi.global.exceptionhandler.exception.InvalidStateException;
import gymmi.global.exceptionhandler.exception.NotHavePermissionException;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.etc.service.S3Service;
import gymmi.workspace.mission.repository.FavoriteMissionRepository;
import gymmi.workspace.mission.repository.MissionRepository;
import gymmi.workspace.objection.repository.ObjectionRepository;
import gymmi.workspace.vote.repository.VoteRepository;
import gymmi.workspace.workspace.controller.request.EditingIntroductionOfWorkspaceRequest;
import gymmi.workspace.workspace.controller.response.*;
import gymmi.workspace.workspace.domain.WorkspaceDrawManger;
import gymmi.workspace.workspace.domain.WorkspaceEditManager;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.workspace.domain.WorkspaceGateChecker;
import gymmi.workspace.workspace.domain.WorkspaceStatus;
import gymmi.workspace.workspace.domain.entity.WorkspaceResult;
import gymmi.workspace.workspace.repository.WorkerRepository;
import gymmi.workspace.workout.repository.WorkoutHistoryRepository;
import gymmi.workspace.workout.repository.WorkoutRecordRepository;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import gymmi.workspace.workspace.repository.WorkspaceResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final ObjectionRepository objectionRepository;
    private final WorkspaceResultRepository workspaceResultRepository;


    public WorkspaceIntroductionResponse getWorkspaceIntroduction(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
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
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);

        boolean matchingResult = workspace.matchesPassword(workspacePassword);
        return new MatchingWorkspacePasswordResponse(matchingResult);
    }

    public CheckingEntranceOfWorkspaceResponse checkEnteringWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
        Worker worker = workerRepository.findByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId())
                .orElseGet(null);

        WorkspaceGateChecker workspaceGateChecker = new WorkspaceGateChecker(workspace, workers);
        boolean isFull = !workspaceGateChecker.canJoin();
        boolean isExist = workspaceGateChecker.canEnter(worker);
        return new CheckingEntranceOfWorkspaceResponse(isExist, isFull);
    }

    public CheckingCreationOfWorkspaceResponse checkCreatingOfWorkspace(User loginedUser) {
        long countOfJoinedWorkspaces =
                workspaceRepository.countsActivateWorkspace(loginedUser.getId());
        boolean canCreate = countOfJoinedWorkspaces < 5;
        return new CheckingCreationOfWorkspaceResponse(canCreate);
    }

    public InsideWorkspaceResponse enterWorkspace(User logiendUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspaceId);
        validateIfWorkerIsInWorkspace(logiendUser.getId(), workspaceId);
        int achievementScore = workspaceRepository.getAchievementScore(workspaceId);
        boolean isObjectionInProgress = objectionRepository.existsByInProgress(workspace.getId());

        return new InsideWorkspaceResponse(workspace, workers, achievementScore, isObjectionInProgress, logiendUser);
    }

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

}
