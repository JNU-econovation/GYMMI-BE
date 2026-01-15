package gymmi.global.common.eventlistener;

import gymmi.user.domain.User;
import gymmi.global.common.eventlistener.event.ObjectionOpenEvent;
import gymmi.global.common.eventlistener.event.WorkoutConfirmationCreatedEvent;
import gymmi.global.common.eventlistener.event.WorkspacePhaseChangedEvent;
import gymmi.global.common.eventlistener.event.WorkspaceStartedEvent;
import gymmi.global.infra.firebase.FirebaseCloudMessageService;
import gymmi.global.infra.firebase.SendingRequest;
import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.objection.repository.ObjectionRepository;
import gymmi.workspace.workspace.repository.WorkerRepository;
import gymmi.workspace.workout.repository.WorkoutHistoryRepository;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Async
public class AlarmEventListener {

    public static final String GYMMI = "GYMMI";

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final ObjectionRepository objectionRepository;
    private final WorkoutHistoryRepository workoutHistoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyWorkspaceStart(WorkspaceStartedEvent event) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(event.getWorkspaceId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
        List<User> users = workers.stream()
                .map(Worker::getUser)
                .collect(Collectors.toList());
        users.remove(workspace.getCreator());
        for (User user : users) {
            SendingRequest request = SendingRequest.builder()
                    .userToken(user.getAlarmToken())
                    .title(GYMMI)
                    .redirectUrl("/workspace/" + workspace.getId())
                    .createdAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .body("워크스페이스가 시작 되었어요!")
                    .build();
            firebaseCloudMessageService.sendMessage(request);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyObjectionOpen(ObjectionOpenEvent event) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(event.getWorkspaceId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
        Objection objection = objectionRepository.findByIdOrThrow(event.getObjectionId());
        WorkoutHistory workoutHistory = workoutHistoryRepository.findByWorkoutConfirmationIdOrThrow(objection.getWorkoutHistory().getWorkoutConfirmation().getId());
        List<User> users = workers.stream()
                .map(Worker::getUser)
                .toList();
        for (User user : users) {
            SendingRequest request = SendingRequest.builder()
                    .userToken(user.getAlarmToken())
                    .title(GYMMI)
                    .redirectUrl("/workspace/" + workspace.getId() + "/workspaceConfirmation/workspaceConfirmationDetail")
                    .createdAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .body("누군가가 운동인증에 이의신청을 했어요!")
                    .build();
            firebaseCloudMessageService.sendMessage(request);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyWorkoutConfirmationCreated(WorkoutConfirmationCreatedEvent event) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(event.getWorkspaceId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
        Worker worker = workerRepository.findWorkerOrThrow(event.getUserId(), workspace.getId());
        List<User> users = workers.stream()
                .map(Worker::getUser)
                .collect(Collectors.toList());
        users.remove(worker.getUser());
        for (User user : users) {
            SendingRequest request = SendingRequest.builder()
                    .userToken(user.getAlarmToken())
                    .title(GYMMI)
                    .redirectUrl("/workspace/" + workspace.getId() + "/workspaceConfirmation")
                    .createdAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .body("새로운 운동인증이 등록되었어요!")
                    .build();
            firebaseCloudMessageService.sendMessage(request);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyWorkspacePhaseChanged(WorkspacePhaseChangedEvent event) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(event.getWorkspaceId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
        List<User> users = workers.stream()
                .map(Worker::getUser)
                .toList();
        for (User user : users) {
            SendingRequest request = SendingRequest.builder()
                    .userToken(user.getAlarmToken())
                    .title(GYMMI)
                    .redirectUrl("/")
                    .createdAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .body("워크스페이스 달성률이 " + event.getWorkspacePhase().getValue() + "%에 도달했어요!")
                    .build();
            firebaseCloudMessageService.sendMessage(request);
        }
    }
}
