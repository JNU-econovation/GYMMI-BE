package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.service.ImageUse;
import gymmi.service.S3Service;
import gymmi.workspace.domain.ObjectionStatus;
import gymmi.workspace.domain.WorkoutMetric;
import gymmi.workspace.domain.WorkspaceGateChecker;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.repository.*;
import gymmi.workspace.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final MissionRepository missionRepository;
    private final WorkoutHistoryRepository workoutHistoryRepository;
    private final WorkoutRecordRepository workoutRecordRepository;
    private final FavoriteMissionRepository favoriteMissionRepository;
    private final ObjectionRepository objectionRepository;
    private final VoteRepository voteRepository;

    private final S3Service s3Service;

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
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        List<Mission> missions = missionRepository.getAllByWorkspaceId(workspaceId);
        List<Mission> favoriteMissions = favoriteMissionRepository.getAllByWorkerId(worker.getId()).stream()
                .map(favoriteMission -> favoriteMission.getMission())
                .toList();

        List<MissionResponse> responses = new ArrayList<>();
        for (Mission mission : missions) {
            boolean isFavorite = favoriteMissions.contains(mission);
            responses.add(new MissionResponse(mission, isFavorite));
        }
        return responses;
    }

    public WorkoutContextResponse getWorkoutContext(
            User loginedUser,
            Long workspaceId,
            Long userId
    ) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        Worker targetWorker = validateIfWorkerIsInWorkspace(userId, workspace.getId());
        List<WorkoutHistory> workoutHistories = workoutHistoryRepository.getAllByWorkerId(targetWorker.getId());
        int firstPlaceScore = workspaceRepository.getFirstPlaceScoreIn(workspace.getId());

        WorkoutMetric workoutMetric = new WorkoutMetric(workoutHistories);
        return new WorkoutContextResponse(
                targetWorker.getNickname(),
                workoutMetric,
                workoutMetric.getScoreGapFrom(firstPlaceScore),
                workoutHistories
        );
    }

    public List<WorkoutRecordResponse> getWorkoutRecordsInWorkoutHistory(
            User loginedUser,
            Long workspaceId,
            Long workoutHistoryId
    ) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutHistoryId(workoutHistoryId);
        workoutHistory.canBeReadIn(workspace);
        List<WorkoutRecord> workoutRecords = workoutRecordRepository.getAllByWorkoutHistoryId(workoutHistoryId);
        return workoutRecords.stream()
                .map(WorkoutRecordResponse::new)
                .toList();
    }

    public CheckingEntranceOfWorkspaceResponse checkEnteringWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
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
                workspaceRepository.getCountsOfJoinedWorkspacesExcludeCompleted(loginedUser.getId());
        boolean canCreate = countOfJoinedWorkspaces < 5;
        return new CheckingCreationOfWorkspaceResponse(canCreate);
    }

    public InsideWorkspaceResponse enterWorkspace(User logiendUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspaceId);
        validateIfWorkerIsInWorkspace(logiendUser.getId(), workspaceId);
        int achievementScore = workspaceRepository.getAchievementScore(workspaceId);
        boolean isObjectionInProgress = objectionRepository.existsByInProgress(workspace.getId());

        return new InsideWorkspaceResponse(workspace, workers, achievementScore, isObjectionInProgress, logiendUser);
    }

    public List<FavoriteMissionResponse> getFavoriteMissions(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        List<FavoriteMission> favoriteMissions = favoriteMissionRepository.getAllByWorkerId(worker.getId());
        return favoriteMissions.stream()
                .map(FavoriteMission::getMission)
                .map(FavoriteMissionResponse::new)
                .toList();
    }

    public WorkoutConfirmationResponse getWorkoutConfirmations(User loginedUser, Long workspaceId, int page) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        List<WorkoutConfirmationOrObjectionProjection> dtos = workoutHistoryRepository.getWorkoutConfirmationAndObjectionDto(workspace.getId(), page);
        dtos.sort(Comparator.comparing(WorkoutConfirmationOrObjectionProjection::getCreatedAt));

        int voteIncompletionCount = 0;
        List<WorkoutConfirmationOrObjectionResponse> responses = new ArrayList<>();
        for (WorkoutConfirmationOrObjectionProjection dto : dtos) {
            if (dto.getType().equals("workoutHistory")) {
                WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutHistoryId(dto.getId());
                WorkoutConfirmation workoutConfirmation = workoutHistory.getWorkoutConfirmation();
                String imagePresignedUrl = s3Service.getPresignedUrl(ImageUse.WORKOUT_CONFIRMATION, workoutConfirmation.getFilename());
                Objection objection = objectionRepository.findByWorkoutConfirmationId(workoutConfirmation.getId()).orElseGet(() -> null);
                responses.add(WorkoutConfirmationOrObjectionResponse.workoutConfirmation(loginedUser, objection, workoutHistory, imagePresignedUrl));
            }
            if (dto.getType().equals("objection")) {
                Objection objection = objectionRepository.getByObjectionId(dto.getId());
                WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutConfirmationId(objection.getWorkoutConfirmation().getId());
                responses.add(WorkoutConfirmationOrObjectionResponse.objection(loginedUser, objection, workoutHistory.getWorker().getUser()));
                if (objection.isInProgress() && !objection.hasVoteBy(worker)) {
                    voteIncompletionCount++;
                }
            }
        }

        return new WorkoutConfirmationResponse(responses, voteIncompletionCount);
    }

    public WorkoutConfirmationDetailResponse getWorkoutConfirmation(User loginedUser, Long workspaceId, Long workoutConfirmationId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutConfirmationId(workoutConfirmationId);

        workoutHistory.canBeReadIn(workspace);
        WorkoutConfirmation workoutConfirmation = workoutHistory.getWorkoutConfirmation();

        String imagePresignedUrl = s3Service.getPresignedUrl(ImageUse.WORKOUT_CONFIRMATION, workoutConfirmation.getFilename());
        Objection objection = objectionRepository.findByWorkoutConfirmationId(workoutConfirmationId)
                .orElseGet(() -> null);

        return new WorkoutConfirmationDetailResponse(workoutHistory.getWorker().getUser(), imagePresignedUrl, workoutConfirmation.getComment(), objection);
    }

    public ObjectionResponse getObjection(User loginedUser, Long workspaceId, Long objectionId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        Objection objection = objectionRepository.getByObjectionId(objectionId);
        objection.canBeReadIn(workspace);

        Integer headCount = workspace.getHeadCount();
        if (objection.isInProgress() && objection.hasVoteBy(worker)) {
            return ObjectionResponse.objectionInProgressWithVoteCompletion(objection, headCount);
        }

        if (objection.isInProgress() && !objection.hasVoteBy(worker)) {
            return ObjectionResponse.objectionInProgressWithVoteInCompletion(objection, headCount);
        }

        WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutConfirmationId(objection.getWorkoutConfirmation().getId());

        workoutHistory.canBeReadIn(workspace);
        return ObjectionResponse.closedObjection(objection, objection.hasVoteBy(worker), workoutHistory.isApproved(), headCount);
    }

    public List<ObjectionAlarmResponse> getObjections(User loginedUser, Long workspaceId, int pageNumber, ObjectionStatus objectionStatus) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        PageRequest pageRequest = PageRequest.of(pageNumber, 10);
        List<Objection> objections = objectionRepository.getAllBy(workspaceId, worker.getId(), objectionStatus, pageRequest);
        return generateObjectionAlarmResponse(worker, objections);
    }

    private List<ObjectionAlarmResponse> generateObjectionAlarmResponse(Worker worker, List<Objection> objections) {
        List<ObjectionAlarmResponse> responses = new ArrayList<>();
        for (Objection objection : objections) {
            WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutConfirmationId(objection.getWorkoutConfirmation().getId());
            boolean voteCompletion = objection.hasVoteBy(worker);
            responses.add(new ObjectionAlarmResponse(objection, workoutHistory.getWorker().getNickname(), voteCompletion));
        }
        return responses;
    }

}
