package gymmi.workspace.workspace.domain;

import gymmi.etc.domain.entity.User;
import gymmi.fixture.UserFixture;
import gymmi.fixture.WorkerFixture;
import gymmi.fixture.WorkspaceFixture;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import org.junit.jupiter.api.Test;

import static gymmi.global.exceptionhandler.message.ErrorCode.NOT_JOINED_WORKSPACE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParticipantValidatorTest {

    @Test
    void 워크스페이스_참여자가_아닌_경우_예외가_발생한다() {
        // given
        User user = UserFixture.defaultUser();
        User user1 = UserFixture.builder().id(1L).build();
        Workspace workspace = WorkspaceFixture.builder(user)
                .id(0L)
                .workspaceStatus(WorkspaceStatus.PREPARING)
                .build();

        Workspace workspace1 = WorkspaceFixture.builder(user)
                .id(1L)
                .workspaceStatus(WorkspaceStatus.PREPARING)
                .build();


        Worker worker = WorkerFixture.builder(user1, workspace1).build();

        // when, then
        assertThatThrownBy(() -> ParticipantValidator.validateParticipant(workspace, worker))
                .hasMessage(NOT_JOINED_WORKSPACE.getMessage());

    }

}
