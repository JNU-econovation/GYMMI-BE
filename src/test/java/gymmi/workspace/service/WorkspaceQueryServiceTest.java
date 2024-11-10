package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.service.S3Service;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.response.WorkoutConfirmationResponse;
import gymmi.workspace.response.WorkoutContextResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class WorkspaceQueryServiceTest extends IntegrationTest {

    @Autowired
    WorkspaceQueryService workspaceQueryService;

    @MockBean
    S3Service s3Service;

    @Test
    void 참여자의_운동_현황을_확인_한다() {
        // given
        User creator = persister.persistUser();
        User user = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS, 100, 2);
        Mission mission = persister.persistMission(workspace, 1);
        Mission mission1 = persister.persistMission(workspace, 5);
        Worker workerCreator = persister.persistWorker(creator, workspace);
        Worker worker = persister.persistWorker(user, workspace);

        persister.persistWorkoutHistoryAndApply(workerCreator, Map.of(mission, 1, mission1, 4));
        persister.persistWorkoutHistoryAndApply(worker, Map.of(mission, 2, mission1, 2));
        persister.persistWorkoutHistoryAndApply(worker, Map.of(mission, 1, mission1, 1));

        // when
        WorkoutContextResponse response = workspaceQueryService.getWorkoutContext(
                creator,
                workspace.getId(),
                user.getId()
        );

        // then
        assertThat(response.getBestDailyScore()).isEqualTo(2 + 10);
        assertThat(response.getGabScoreFromFirst()).isEqualTo(21 - (12 + 6));
        assertThat(response.getTotalContributedScore()).isEqualTo(12 + 6);
        assertThat(response.getTotalWorkoutCount()).isEqualTo(2);
    }

    @Test
    void 워크스페이스의_운동_인증_목록을_확인_한다() {
        // given
        User creator = persister.persistUser();
        User user = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS);
        Worker creatorWorker = persister.persistWorker(creator, workspace);
        Worker userWorker = persister.persistWorker(user, workspace);
        Mission mission = persister.persistMission(workspace, 1);
        Mission mission1 = persister.persistMission(workspace, 5);
        WorkoutProof workoutProof = new WorkoutProof("creator", "a");
        WorkoutProof workoutProof1 = new WorkoutProof("user", "b");

        WorkoutHistory workoutHistory = persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 2, mission1, 2), workoutProof);
        WorkoutHistory workoutHistory1 = persister.persistWorkoutHistoryAndApply(userWorker, Map.of(mission, 2, mission1, 2), workoutProof1);

        // when
        List<WorkoutConfirmationResponse> responses = workspaceQueryService.getWorkoutConfirmations(user, workspace.getId(), 0);

        // then
        assertThat(responses).hasSize(2);
        WorkoutConfirmationResponse response = responses.get(0);
        assertThat(response.getNickname()).isEqualTo(user.getNickname());
        assertThat(response.getProfileImageUrl()).isEqualTo(user.getProfileImageName());
        assertThat(response.getWorkoutConfirmationId()).isEqualTo(workoutHistory1.getWorkoutProof().getId());
        WorkoutConfirmationResponse response1 = responses.get(1);
        assertThat(response1.getNickname()).isEqualTo(creator.getNickname());
        assertThat(response1.getProfileImageUrl()).isEqualTo(creator.getProfileImageName());
        assertThat(response1.getWorkoutConfirmationId()).isEqualTo(workoutHistory.getWorkoutProof().getId());
    }

}
