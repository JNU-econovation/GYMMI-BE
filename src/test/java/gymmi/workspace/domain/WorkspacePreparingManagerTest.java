package gymmi.workspace.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.entity.User;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import java.util.Arrays;
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

            // when
            Worker worker = workspacePreparingManager.allow(user, workspace.getPassword());

            // then
            assertThat(workspacePreparingManager.getWorkers()).hasSize(2);
            assertThat(worker.getWorkspace()).isEqualTo(workspace);
        }

        @Test
        void 워크스페이스_비밀번호가_일치하지_않는_경우_예외가_발생한다() {
            // given
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, 1);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            User user = Instancio.of(User.class).create();
            String wrongPassword = "1235";

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.allow(user, wrongPassword))
                    .hasMessage(ErrorCode.NOT_MATCHED_PASSWORD.getMessage());
        }   

        @Test
        void 워크스페이스_인원이_가득_찬_경우_예외가_발생한다() {
            // given
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, Workspace.MIN_HEAD_COUNT);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            User user = Instancio.of(User.class).create();

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.allow(user, workspace.getPassword()))
                    .hasMessage(ErrorCode.FULL_WORKSPACE.getMessage());
        }

        @Test
        void 워크스페이스가_준비중이_아닌_경우_예외가_발생한다() {
            // given
            WorkspaceStatus workspaceStatus = getWorkspaceStatusExcluding(WorkspaceStatus.PREPARING);
            Workspace workspace = getWorkspace("1234", workspaceStatus, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, 1);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            User user = Instancio.of(User.class).create();

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.allow(user, workspace.getPassword()))
                    .hasMessage(ErrorCode.ALREADY_ACTIVATED_WORKSPACE.getMessage());
        }

        @Test
        void 워크스페이스에_이미_참여_한_경우_예외가_발생_한다() {
            // given
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, 1);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            User user = workers.get(0).getUser();

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.allow(user, workspace.getPassword()))
                    .hasMessage(ErrorCode.ALREADY_JOINED_WORKSPACE.getMessage());
        }
    }

    @Nested
    class 워크스페이스_참여자_내보내기 {

        @Test
        void 워크스페이스_참여자가_아닌_경우_예외가_발생한다() {
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            Workspace workspace1 = Instancio.of(Workspace.class)
                    .filter(Select.field(Workspace::getId), (Long id) -> id != workspace.getId())
                    .create();
            List<Worker> workers = getWorkers(workspace, 1);
            Worker worker = getWorker(workspace1, workspace1.getCreator(), workers.get(0).getId());

            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.release(worker))
                    .hasMessage(ErrorCode.NOT_JOINED_WORKSPACE.getMessage());
        }

        @Test
        void 이미_워크스페이스가_이미_활성화된_경우_예외가_발생한다() {
            // given
            WorkspaceStatus workspaceStatus = getWorkspaceStatusExcluding(WorkspaceStatus.PREPARING);
            Workspace workspace = getWorkspace("1234", workspaceStatus, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, Workspace.MIN_HEAD_COUNT);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.release(workers.get(0)))
                    .hasMessage(ErrorCode.ALREADY_ACTIVATED_WORKSPACE.getMessage());
        }

        @Test
        void 방장이_워크스페이스_참여자가_남아있을때_나가는_경우_예외가_발생한다() {
            // given
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, 1);
            Worker creator = getWorker(workspace, workspace.getCreator(), workers.get(0).getId());
            workers.add(creator);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.release(creator))
                    .hasMessage(ErrorCode.EXIST_WORKERS_EXCLUDE_CREATOR.getMessage());
        }

        @Test
        void 워크스페이스_참여자를_내보낸다() {
            // given
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            Worker worker = getWorker(workspace, workspace.getCreator());
            List<Worker> workers = List.of(worker);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            // when
            WorkerLeavedEvent result = workspacePreparingManager.release(worker);

            // then
            assertThat(workspacePreparingManager.getWorkers()).isEmpty();
            assertThat(result.getWorker()).isEqualTo(worker);
            assertThat(result.isLastOne()).isTrue();
        }
    }

    @Nested
    class 워크스페이스_시작 {

        @Test
        void 방장이_아닌_경우_예외가_발생한다() {
            // given
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, 1);

            User user = Instancio.of(User.class)
                    .filter(Select.field(User::getId), (Long id) -> id != workspace.getCreator().getId())
                    .create();
            Worker notCreator = getWorker(workspace, user, workers.get(0).getId());
            workers.add(notCreator);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.startBy(notCreator))
                    .hasMessage(ErrorCode.NOT_WORKSPACE_CREATOR.getMessage());
        }

        @Test
        void 이미_워크스페이스가_활성화된_경우_예외가_발생한다() {
            // given
            WorkspaceStatus activatedWorkspaceStatus = getWorkspaceStatusExcluding(WorkspaceStatus.PREPARING);
            Workspace workspace = getWorkspace("1234", activatedWorkspaceStatus, Workspace.MIN_HEAD_COUNT);
            List<Worker> workers = getWorkers(workspace, 1);
            Worker creator = getWorker(workspace, workspace.getCreator(), workers.get(0).getId());
            workers.add(creator);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.startBy(creator))
                    .hasMessage(ErrorCode.ALREADY_ACTIVATED_WORKSPACE.getMessage());
        }

        @Test
        void 최소_인원을_만족하지_않는_경우_예외가_발생한다() {
            // given;
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, Workspace.MIN_HEAD_COUNT);
            Worker creator = getWorker(workspace, workspace.getCreator());
            List<Worker> workers = List.of(creator);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.startBy(creator))
                    .hasMessage(ErrorCode.BELOW_MINIMUM_WORKER.getMessage());
        }

        @Test
        void 워크스페이스를_시작한다() {
            // given
            Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, 5);
            List<Worker> workers = getWorkers(workspace, 2);
            Long id = workers.get(0).getId();
            Long id1 = workers.get(1).getId();
            Worker creator = getWorker(workspace, workspace.getCreator(), id, id1);
            workers.add(creator);
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);

            // when
            workspacePreparingManager.startBy(creator);

            // then
            assertThat(workspace.getStatus()).isEqualTo(WorkspaceStatus.IN_PROGRESS);
        }
    }

    private Worker getWorker(Workspace workspace, User user, Long... excludingWorkerIds) {
        List<Long> excludingIds = Arrays.stream(excludingWorkerIds).toList();
        return Instancio.of(Worker.class)
                .set(Select.field(Worker::getWorkspace), workspace)
                .set(Select.field(Worker::getUser), user)
                .filter(Select.field(Worker::getId), (Long id) -> !excludingIds.contains(id))
                .create();
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

    private WorkspaceStatus getWorkspaceStatusExcluding(WorkspaceStatus... workspaceStatus) {
        return Instancio.gen()
                .enumOf(WorkspaceStatus.class)
                .excluding(workspaceStatus)
                .get();
    }
}
