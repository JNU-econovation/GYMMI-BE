package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.firebase.FirebaseTestConfig;
import gymmi.global.firebase.FirebaseCloudMessageService;
import gymmi.helper.Persister;
import gymmi.service.S3Service;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.repository.WorkspaceRepository;
import gymmi.workspace.request.ObjectionRequest;
import gymmi.workspace.request.WorkingMissionInWorkspaceRequest;
import gymmi.workspace.request.WorkoutRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@Import({Persister.class, FirebaseTestConfig.class})
public class PushingAlarmTest {

    //todo: need to clean up database after test

    int WAIT_TIME_MS = 1500;

    @Autowired
    WorkspaceCommandService workspaceCommandService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @MockBean
    FirebaseCloudMessageService firebaseCloudMessageService;

    @MockBean
    S3Service s3Service;

    @Autowired
    Persister persister;

    @Test
    void 워크스페이스_시작시_방장을_제외한_참여자들에게_알림이_푸쉬된다() throws InterruptedException {
        // given
        User user = persister.persistUser();
        User user1 = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(user, WorkspaceStatus.PREPARING);
        Worker worker = persister.persistWorker(user, workspace);
        Worker worker1 = persister.persistWorker(user1, workspace);

        // when
        workspaceCommandService.startWorkspace(user, workspace.getId());

        // then
        Thread.sleep(WAIT_TIME_MS);
        then(firebaseCloudMessageService).should(times(1)).sendMessage(any());
    }

    @Test
    void 운동인증_등록시_본인을_제외한_참여자들에게_알림이_푸쉬된다() throws InterruptedException {
        // given
        User user = persister.persistUser();
        User user1 = persister.persistUser();
        User user2 = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(user, WorkspaceStatus.IN_PROGRESS, 100, 3);
        Worker worker = persister.persistWorker(user, workspace);
        Worker worker1 = persister.persistWorker(user1, workspace);
        Worker worker2 = persister.persistWorker(user2, workspace);
        Mission mission = persister.persistMission(workspace, 10);
        List<WorkingMissionInWorkspaceRequest> requests = List.of(
                new WorkingMissionInWorkspaceRequest(mission.getId(), 1)
        );
        WorkoutRequest request = Instancio.of(WorkoutRequest.class)
                .set(field(WorkoutRequest::getMissions), requests)
                .set(field(WorkoutRequest::getWillLink), false)
                .create();
        given(s3Service.copy(any(), any(), any())).willReturn(UUID.randomUUID().toString());

        // when
        workspaceCommandService.workMissionsInWorkspace(user, workspace.getId(), request);

        // then
        Thread.sleep(1000);
        then(firebaseCloudMessageService).should(times(2)).sendMessage(any());
    }

    @Test
    void 이의신청시_모든_참여자들에게_알림이_푸쉬된다() throws InterruptedException {
        // given
        User creator = persister.persistUser();
        User user = persister.persistUser();
        User user1 = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS);
        Worker creatorWorker = persister.persistWorker(creator, workspace);
        Worker userWorker = persister.persistWorker(user, workspace);
        Worker userWorker1 = persister.persistWorker(user1, workspace);
        Mission mission = persister.persistMission(workspace, 1);
        Mission mission1 = persister.persistMission(workspace, 5);
        WorkoutHistory workoutHistory = persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 2, mission1, 2));
        ObjectionRequest request = new ObjectionRequest("이유");
        Long workoutConfirmationId = workoutHistory.getWorkoutConfirmation().getId();

        // when
        workspaceCommandService.objectToWorkoutConfirmation(user, workspace.getId(), workoutConfirmationId, request);

        // then
        Thread.sleep(1000);
        then(firebaseCloudMessageService).should(times(3)).sendMessage(any());
    }

}
