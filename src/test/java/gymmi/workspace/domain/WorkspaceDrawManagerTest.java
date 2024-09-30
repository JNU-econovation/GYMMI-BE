package gymmi.workspace.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.exception.message.ErrorCode;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

class WorkspaceDrawManagerTest {

    @Test
    void 워크스페이스가_완료_되지_않은_경우_예외가_발생한다() {
        // given
        WorkspaceStatus workspaceStatus = Instancio.gen()
                .enumOf(WorkspaceStatus.class)
                .excluding(WorkspaceStatus.COMPLETED, WorkspaceStatus.FULLY_COMPLETED)
                .get();
        Workspace workspace = Instancio.of(Workspace.class)
                .set(Select.field(Workspace::getStatus), workspaceStatus)
                .create();

        List<Worker> workers = Instancio.ofList(Worker.class)
                .size(2)
                .set(Select.field(Worker::getWorkspace), workspace)
                .create();

        // when, then
        assertThatThrownBy(() -> new WorkspaceDrawManager(workspace, workers))
                .hasMessage(ErrorCode.NOT_REACHED_WORKSPACE_GOAL_SCORE.getMessage());
    }

    @Test
    void 테스크_뽑기를_하지_않은_경우_테스크를_뽑는다() {
        // given
        Workspace workspace = Instancio.of(Workspace.class)
                .set(Select.field(Workspace::getStatus), WorkspaceStatus.COMPLETED)
                .create();
        List<Worker> workers = Instancio.ofList(Worker.class)
                .size(3)
                .set(Select.field(Worker::getWorkspace), workspace)
                .withUnique(Select.field(Worker::getId))
                .supply(
                        Select.field(Worker::getTask), () -> Instancio.of(Task.class)
                                .withUnique(Select.field(Task::getId))
                                .set(Select.field(Task::isPicked), false)
                                .create()
                ).create();
        WorkspaceDrawManager workspaceDrawManager = new WorkspaceDrawManager(workspace, workers);
        assertThat(
                workspaceDrawManager.getTasks(workers.get(0))
                        .stream()
                        .filter(t -> t.isPicked() == true)
                        .count()
        ).isEqualTo(0);

        // when
        workspaceDrawManager.drawIfNotPicked();

        // then
        assertThat(workspace.getStatus()).isEqualTo(WorkspaceStatus.FULLY_COMPLETED);
        assertThat(
                workspaceDrawManager.getTasks(workers.get(0))
                        .stream()
                        .filter(t -> t.isPicked() == true)
                        .count()
        ).isEqualTo(1);
    }

}
