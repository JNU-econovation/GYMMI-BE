package gymmi.workspace.domain;

import static gymmi.workspace.domain.WorkspaceWithWorkersConsistencyValidator.validateMeetMinHeadCount;
import static gymmi.workspace.domain.WorkspaceWithWorkersConsistencyValidator.validateWorkersConsistency;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import java.util.Collections;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

class WorkspaceWithWorkersConsistencyValidatorTest {

    @Test
    void 워크스페이스_최소_참여자_수를_만족하지_않는_경우_예외가_발생한다() {
        // given
        Workspace workspace = Instancio.of(Workspace.class)
                .set(Select.field(Workspace::getHeadCount), 3)
                .create();
        List<Worker> workers = Instancio.ofList(Worker.class)
                .size(Workspace.MIN_HEAD_COUNT - 1)
                .set(Select.field(Worker::getWorkspace), workspace)
                .create();

        // when, then
        assertThatThrownBy(() -> validateMeetMinHeadCount(workers))
                .hasMessage(ErrorCode.NOT_CONSISTENT_WORKERS_COUNT.getMessage());
    }

    @Test
    void 워크스페이스_참여자가_아닌_경우_예외가_발생한다() {
        // given
        Workspace workspace = Instancio.of(Workspace.class).create();

        Worker worker = Instancio.of(Worker.class)
                .set(Select.field(Worker::getWorkspace), workspace)
                .create();
        Worker worker1 = Instancio.of(Worker.class)
                .filter(Select.field(Worker::getWorkspace), (Workspace ws) -> !ws.equals(workspace))
                .create();
        List<Worker> workers = List.of(worker, worker1);

        // when, then
        assertThatThrownBy(() -> validateWorkersConsistency(workspace, workers))
                .hasMessage(ErrorCode.EXIST_NOT_JOINED_WORKER.getMessage());
    }

    @Test
    void 워크스페이스_참여자가_없는_경우_예외가_발생한다() {
        // given
        Workspace workspace = Instancio.of(Workspace.class).create();
        List<Worker> workers = Collections.emptyList();

        // when, then
        assertThatThrownBy(() -> validateWorkersConsistency(workspace, workers))
                .hasMessage(ErrorCode.NOT_CONSISTENT_WORKERS_COUNT.getMessage());
    }

}
