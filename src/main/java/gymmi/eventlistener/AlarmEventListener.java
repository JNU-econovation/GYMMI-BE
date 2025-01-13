package gymmi.eventlistener;

import gymmi.entity.User;
import gymmi.eventlistener.event.ObjectionOpenEvent;
import gymmi.eventlistener.event.WorkoutConfirmationCreatedEvent;
import gymmi.eventlistener.event.WorkspaceStartedEvent;
import gymmi.global.firebase.FirebaseCloudMessageService;
import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.WorkoutHistory;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.repository.ObjectionRepository;
import gymmi.workspace.repository.WorkerRepository;
import gymmi.workspace.repository.WorkoutHistoryRepository;
import gymmi.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Async
public class AlarmEventListener {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final ObjectionRepository objectionRepository;
    private final WorkoutHistoryRepository workoutHistoryRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyWorkspaceStart(WorkspaceStartedEvent event) {
        System.out.println("워크스페이스 시작됨");
        Workspace workspace = workspaceRepository.getWorkspaceById(event.getWorkspaceId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
        List<User> users = workers.stream()
                .map(Worker::getUser)
                .toList();
        users.remove(workspace.getCreator());
        for (User user : users) {
            firebaseCloudMessageService.sendMessage(user.getAlarmToken(), workspace.getName(), "워크스페이스 시작됨.");
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyObjectionOpen(ObjectionOpenEvent event) {
        Workspace workspace = workspaceRepository.getWorkspaceById(event.getWorkspaceId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
        Objection objection = objectionRepository.getByObjectionId(event.getObjectionId());
        WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutConfirmationId(objection.getWorkoutConfirmation().getId());
        List<User> users = workers.stream()
                .map(Worker::getUser)
                .toList();
        for (User user : users) {
            firebaseCloudMessageService.sendMessage(user.getAlarmToken(), workspace.getName(), "이의신청 추가됨.");
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyWorkoutConfirmationCreated(WorkoutConfirmationCreatedEvent event) {
        Workspace workspace = workspaceRepository.getWorkspaceById(event.getWorkspaceId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
        Worker worker = workerRepository.getByUserIdAndWorkspaceId(event.getUserId(), workspace.getId());
        List<User> users = workers.stream()
                .map(Worker::getUser)
                .toList();
        users.remove(worker);
        for (User user : users) {
            firebaseCloudMessageService.sendMessage(user.getAlarmToken(), workspace.getName(), "운동인증 추가됨.");
        }
    }
}
