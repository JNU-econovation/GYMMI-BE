package gymmi.eventlistener;

import gymmi.entity.User;
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

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyWorkspaceStart(WorkspaceStartedEvent workspaceStartedEvent) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceStartedEvent.getWorkspaceId());
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
    public void notifyObjectionOpen(Long workspaceId, Long objectionId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspaceId);
        Objection objection = objectionRepository.getByObjectionId(objectionId);
        WorkoutHistory workoutHistory = workoutHistoryRepository.getByWorkoutConfirmationId(objection.getWorkoutConfirmation().getId());
        List<User> users = workers.stream()
                .map(Worker::getUser)
                .toList();
        for (User user : users) {
            firebaseCloudMessageService.sendMessage(user.getAlarmToken(), workspace.getName(), "이의신청 추가됨.");
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyWorkoutConfirmationCreated(Long workspaceId, Long userId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspaceId);
        Worker worker = workerRepository.getByUserIdAndWorkspaceId(userId, workspace.getId());
        List<User> users = workers.stream()
                .map(Worker::getUser)
                .toList();
        users.remove(worker);
        for (User user : users) {
            firebaseCloudMessageService.sendMessage(user.getAlarmToken(), workspace.getName(), "운동인증 추가됨.");
        }
    }
}
