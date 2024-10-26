package gymmi.workspace.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.entity.User;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Mission;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.request.CreatingWorkspaceRequest;
import gymmi.workspace.request.MissionRequest;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

class WorkspaceInitializerTest {
    @Test
    void 워크스페이스_미션_개수가_15개를_초과_하는_경우_예외가_발생한다() {
        // given
        WorkspaceInitializer workspaceInitializer = new WorkspaceInitializer();
        User user = Instancio.create(User.class);
        int missionSize = 16;

        CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
                .goalScore(Workspace.MIN_GOAL_SCORE)
                .headCount(Workspace.MIN_HEAD_COUNT)
                .name("지미")
                .task(Instancio.gen().string().get())
                .missionBoard(
                        Instancio.ofList(MissionRequest.class)
                                .size(missionSize)
                                .set(Select.field(MissionRequest::getMission),
                                        Instancio.gen().string().maxLength(Mission.MAX_NAME_LENGTH).get())
                                .set(Select.field(MissionRequest::getScore), Mission.MIN_SCORE)
                                .create()
                ).build();

        // when, then
        assertThatThrownBy(() -> workspaceInitializer.init(user, request))
                .hasMessage(ErrorCode.INVALID_WORKSPACE_MISSION_SIZE.getMessage());
    }

    @Test
    void 워크스페이스를_초기화_한다() {
        // given
        WorkspaceInitializer workspaceInitializer = new WorkspaceInitializer();
        User user = Instancio.create(User.class);

        CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
                .goalScore(Workspace.MIN_GOAL_SCORE)
                .headCount(Workspace.MIN_HEAD_COUNT)
                .name("지미")
                .task(Instancio.gen().string().get())
                .missionBoard(
                        List.of(Instancio.of(MissionRequest.class)
                                .set(Select.field(MissionRequest::getMission),
                                        Instancio.gen().string().maxLength(Mission.MAX_NAME_LENGTH).get())
                                .set(Select.field(MissionRequest::getScore), Mission.MIN_SCORE)
                                .create())
                ).build();

        // when
        workspaceInitializer.init(user, request);

        // then
        assertThat(workspaceInitializer.getWorkspace().getName()).isEqualTo(request.getName());
        assertThat(workspaceInitializer.getWorker().getUser()).isEqualTo(user);
        assertThat(workspaceInitializer.getMissions()).hasSize(1);
    }

}
