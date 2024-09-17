package gymmi.workspace.domain;

import static gymmi.workspace.domain.WorkspaceWithWorkersConsistencyValidator.validateMeetMinHeadCount;
import static gymmi.workspace.domain.WorkspaceWithWorkersConsistencyValidator.validateWorkersConsistency;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.exception.message.ErrorCode;
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
                .hasMessage(ErrorCode.NOT_CONSISTENT_WORKERS.getMessage());
    }

    @Test
    void 워크스페이스_참여자가_아닌_경우_예외가_발생한다() {
        // given
        Workspace workspace = Instancio.of(Workspace.class).create();
        Workspace workspace1 = Instancio.of(Workspace.class).create();

        Worker worker = Instancio.of(Worker.class)
                .set(Select.field(Worker::getWorkspace), workspace)
                .create();
        Worker worker1 = Instancio.of(Worker.class)
                .set(Select.field(Worker::getWorkspace), workspace1)
                .create();
        List<Worker> workers = List.of(worker, worker1);

        // when, then
        assertThatThrownBy(() -> validateWorkersConsistency(workspace, workers))
                .hasMessage(ErrorCode.EXIST_NOT_JOINED_WORKER.getMessage());
    }

    @Test
    void 워크스페이스_참여자_수_정합성이_안맞는_경우_예외가_발생한다() {
        // given
        Workspace workspace = Instancio.of(Workspace.class).create();
        int workersSize = Instancio.gen().ints().min(Workspace.MAX_HEAD_COUNT + 1).get();
        List<Worker> workers = Instancio.ofList(Worker.class)
                .size(workersSize)
                .set(Select.field(Worker::getWorkspace), workspace)
                .withUnique(Select.field(Worker::getId))
                .create();

        // when, then
        assertThatThrownBy(() -> validateWorkersConsistency(workspace, workers))
                .hasMessage(ErrorCode.NOT_CONSISTENT_WORKERS.getMessage());
    }

    @Test
    void 워크스페이스_참여자가_없는_경우_예외가_발생한다() {
        // given
        Workspace workspace = Instancio.of(Workspace.class).create();
        List<Worker> workers = Collections.emptyList();

        // when, then
        assertThatThrownBy(() -> validateWorkersConsistency(workspace, workers))
                .hasMessage(ErrorCode.NOT_CONSISTENT_WORKERS.getMessage());
    }

}
