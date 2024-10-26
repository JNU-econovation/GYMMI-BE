package gymmi.workspace.service;

import static org.assertj.core.api.Assertions.assertThat;

import gymmi.entity.User;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.domain.entity.Mission;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.response.WorkoutContextResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class WorkspaceQueryServiceTest extends IntegrationTest {

    @Autowired
    WorkspaceQueryService workspaceQueryService;

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

}
