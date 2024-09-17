package gymmi.workspace.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.entity.User;
import gymmi.exception.message.ErrorCode;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class WorkspacePreparingManagerTest {

    @Nested
    class 워크스페이스_참여_허락 {

        @Test
        void 사용자에게_워크스페이스_참가를_허락한다() {
            // given
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, 1);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            User user = Instancio.of(User.class).create();
            String taskName = Instancio.gen().string().get();

            // when
            Worker worker = workspacePreparingManager.allow(user, "1234", taskName);

            // then
            assertThat(workspacePreparingManager.getWorkers()).hasSize(2);
            assertThat(worker.getWorkspace()).isEqualTo(workspace);
            assertThat(worker.getTask().getName()).isEqualTo(taskName);
        }

        @Test
        void 워크스페이스_비밀번호가_일치하지_않는_경우_예외가_발생한다() {
            // given
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, 1);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            User user = Instancio.of(User.class).create();
            String taskName = Instancio.gen().string().get();

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.allow(user, "1235", taskName))
                    .hasMessage(ErrorCode.NOT_MATCHED_PASSWORD.getMessage());
        }

        @Test
        void 워크스페이스_인원이_가득_찬_경우_예외가_발생한다() {
            // given
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, 2);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            User user = Instancio.of(User.class).create();
            String taskName = Instancio.gen().string().get();

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.allow(user, "1234", taskName))
                    .hasMessage(ErrorCode.FULL_WORKSPACE.getMessage());
        }

        @Test
        void 워크스페이스가_준비중이_아닌_경우_예외가_발생한다() {
            // given
            WorkspaceStatus workspaceStatus = Instancio.gen()
                    .enumOf(WorkspaceStatus.class)
                    .excluding(WorkspaceStatus.PREPARING)
                    .get();
            Workspace workspace = getWorkspace("1234", workspaceStatus, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, 1);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            User user = Instancio.of(User.class).create();
            String taskName = Instancio.gen().string().get();

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.allow(user, "1234", taskName))
                    .hasMessage(ErrorCode.ALREADY_ACTIVATED_WORKSPACE.getMessage());
        }

        @Test
        void 워크스페이스에_이미_참여_한_경우_예외가_발생_한다() {
            // given
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, 1);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            User user = workers.get(0).getUser();
            String taskName = Instancio.gen().string().get();

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.allow(user, "1234", taskName))
                    .hasMessage(ErrorCode.ALREADY_JOINED_WORKSPACE.getMessage());
        }
    }

    private List<Worker> getWorkers(Workspace workspace, int size) {
        List<Worker> workers = Instancio.ofList(Worker.class)
                .size(size)
                .set(Select.field(Worker::getWorkspace), workspace)
                .withUnique(Select.field(Worker::getId))
                .create();
        return workers;
    }

    private Workspace getWorkspace(String password, WorkspaceStatus workspaceStatus, int headCount) {
        Workspace workspace = Instancio.of(Workspace.class)
                .set(Select.field(Workspace::getStatus), workspaceStatus)
                .set(Select.field(Workspace::getPassword), password)
                .set(Select.field(Workspace::getHeadCount), headCount)
                .create();
        return workspace;
    }
}
