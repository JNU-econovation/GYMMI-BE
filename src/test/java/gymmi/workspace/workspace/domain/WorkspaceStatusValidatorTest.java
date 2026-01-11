package gymmi.workspace.workspace.domain;

import gymmi.etc.domain.entity.User;
import gymmi.fixture.UserFixture;
import gymmi.fixture.WorkerFixture;
import gymmi.fixture.WorkspaceFixture;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import org.junit.jupiter.api.Test;

import static gymmi.global.exceptionhandler.message.ErrorCode.INACTIVE_WORKSPACE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class WorkspaceStatusValidatorTest {

    @Test
    void 워크스페이스가_진행중이_아닌_경우_예외가_발생한다() {
        // given
        User user = UserFixture.defaultUser();
        Workspace workspace = WorkspaceFixture.builder(user)
                .id(1L)
                .workspaceStatus(WorkspaceStatus.COMPLETED)
                .build();

        // when, then
        assertThatThrownBy(() -> WorkspaceStatusValidator.validateWorkspaceIsInProgress(workspace))
                .hasMessage(INACTIVE_WORKSPACE.getMessage());
    }

}
