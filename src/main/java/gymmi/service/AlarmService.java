package gymmi.service;

import gymmi.entity.User;
import gymmi.global.firebase.FirebaseCloudMessageService;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.repository.WorkerRepository;
import gymmi.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;

    public void notifyWorkspaceStart(Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspaceId);
        List<User> users = workers.stream()
                .map(Worker::getUser)
                .toList();
        users.remove(workspace.getCreator());
        for (User user : users) {
            firebaseCloudMessageService.sendMessage(user.getAlarmToken(), workspace.getName(), "워크스페이스 시작됨.");
        }
    }

}
