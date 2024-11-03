package gymmi.workspace.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.entity.User;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

class WorkspaceEditManagerTest {

    @Test
    void 방장이_아닌_경우_예외가_발생한다() {
        // given
        Workspace workspace = Instancio.of(Workspace.class)
                .create();
        Worker worker = Instancio.of(Worker.class)
                .set(Select.field(Worker::getWorkspace), workspace)
                .filter(Select.field(Worker::getUser), (User user) -> !user.equals(workspace.getCreator()))
                .create();

        //when, then
        assertThatThrownBy(() -> new WorkspaceEditManager(workspace, worker))
                .hasMessage(ErrorCode.NOT_WORKSPACE_CREATOR.getMessage());
    }

}
