package gymmi.workspace.workout.service;

import gymmi.entity.User;
import gymmi.eventlistener.event.ImageValidationEvent;
import gymmi.eventlistener.event.LinkToPhotoFeedEvent;
import gymmi.eventlistener.event.WorkoutConfirmationCreatedEvent;
import gymmi.eventlistener.event.WorkspacePhaseChangedEvent;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.service.ImageUse;
import gymmi.service.S3Service;
import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.objection.repository.ObjectionRepository;
import gymmi.workspace.workout.controller.request.WorkoutRequest;
import gymmi.workspace.workout.controller.response.*;
import gymmi.workspace.workout.domain.*;
import gymmi.workspace.workout.domain.entity.WorkoutConfirmation;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workout.domain.entity.WorkoutRecord;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.workspace.repository.WorkerRepository;
import gymmi.workspace.workout.repository.WorkoutHistoryRepository;
import gymmi.workspace.workout.repository.WorkoutRecordRepository;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final WorkoutRequestMapper workoutRequestMapper;
    private final WorkoutHistoryRepository workoutHistoryRepository;
    private final WorkoutRecordRepository workoutRecordRepository;
    private final WorkoutValidator workoutValidator;
    private final S3Service s3Service;
    private final ObjectionRepository objectionRepository;



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
        List<WorkoutRecord> workoutRecords = workoutRequestMapper.createWorkoutRecords(workspace, workoutHistory, workoutRequest.getMissions());
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



    public WorkoutConfirmationDetailResponse getWorkoutConfirmation(User loginedUser, Long workspaceId, Long workoutConfirmationId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        WorkoutHistory workoutHistory = workoutHistoryRepository.findByWorkoutConfirmationIdOrThrow(workoutConfirmationId);

//        workoutHistory.canBeReadIn(workspace);
        WorkoutConfirmation workoutConfirmation = workoutHistory.getWorkoutConfirmation();

        String imagePresignedUrl = s3Service.getPresignedUrl(ImageUse.WORKOUT_CONFIRMATION, workoutConfirmation.getFilename());
        Objection objection = objectionRepository.findByWorkoutHistoryId(workoutConfirmationId)
                .orElseGet(() -> null);

        return new WorkoutConfirmationDetailResponse(workoutHistory.getWorker().getUser(), imagePresignedUrl, workoutConfirmation.getComment(), objection);
    }

    private Worker validateIfWorkerIsInWorkspace(Long userId, Long workspaceId) {
        return workerRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotHavePermissionException(ErrorCode.NOT_JOINED_WORKSPACE));
    }

    public WorkoutConfirmationResponse getWorkoutConfirmations(User loginedUser, Long workspaceId, int page) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
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
                Objection objection = objectionRepository.findByWorkoutHistoryId(workoutConfirmation.getId()).orElseGet(() -> null);
                responses.add(WorkoutConfirmationOrObjectionResponse.workoutConfirmation(loginedUser, objection, workoutHistory, imagePresignedUrl));
            }
            if (dto.getType().equals("objection")) {
                Objection objection = objectionRepository.findByIdOrThrow(dto.getId());
                WorkoutHistory workoutHistory = workoutHistoryRepository.findByWorkoutConfirmationIdOrThrow(objection.getWorkoutHistory().getWorkoutConfirmation().getId());
                responses.add(WorkoutConfirmationOrObjectionResponse.objection(loginedUser, objection, workoutHistory.getWorker().getUser()));
//                if (objection.isInProgress() && !objection.hasVoteBy(worker)) {
//                    voteIncompletionCount++;
//                }
            }
        }

        return new WorkoutConfirmationResponse(responses, voteIncompletionCount);
    }

    public List<WorkoutRecordResponse> getWorkoutRecordsInWorkoutHistory(
            User loginedUser,
            Long workspaceId,
            Long workoutHistoryId
    ) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutHistoryId(workoutHistoryId);
//        workoutHistory.canBeReadIn(workspace);
        List<WorkoutRecord> workoutRecords = workoutRecordRepository.getAllByWorkoutHistoryId(workoutHistoryId);
        return workoutRecords.stream()
                .map(WorkoutRecordResponse::new)
                .toList();
    }

    public WorkoutContextResponse getWorkoutContext(
            User loginedUser,
            Long workspaceId,
            Long userId
    ) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
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
}
