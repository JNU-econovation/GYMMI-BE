package gymmi.workspace.domain.entity;

import gymmi.entity.User;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.service.domain.workspace.WorkspaceStatus;
import gymmi.workspace.service.domain.workspace.Worker;
import gymmi.workspace.service.domain.workspace.Workspace;
import gymmi.workspace.service.domain.workspace.WorkspaceCreationValidator;
import gymmi.workspace.service.domain.workspace.WorkspaceStarter;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkspaceStarterTest {


    @Test
    void 방장이_아닌_경우_예외가_발생한다() {
        // given
        Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, WorkspaceCreationValidator.MIN_HEAD_COUNT);
        List<Worker> workers = getWorkers(workspace, 1);

        User user = Instancio.of(User.class)
                .filter(Select.field(User::getId), (Long id) -> id != workspace.getCreator().getId())
                .create();
        Worker notCreator = getWorker(workspace, user, workers.get(0).getId());
        workers.add(notCreator);
        WorkspaceStarter workspaceStarter = new WorkspaceStarter(workspace, workers);

        // when, then
        assertThatThrownBy(() -> workspaceStarter.startBy(notCreator))
                .hasMessage(ErrorCode.NOT_WORKSPACE_CREATOR.getMessage());
    }

    @Test
    void 이미_워크스페이스가_활성화된_경우_예외가_발생한다() {
        // given
        WorkspaceStatus activatedWorkspaceStatus = getWorkspaceStatusExcluding(WorkspaceStatus.PREPARING);
        Workspace workspace = getWorkspace("1234", activatedWorkspaceStatus, WorkspaceCreationValidator.MIN_HEAD_COUNT);
        List<Worker> workers = getWorkers(workspace, 1);
        Worker creator = getWorker(workspace, workspace.getCreator(), workers.get(0).getId());
        workers.add(creator);
        WorkspaceStarter workspaceStarter = new WorkspaceStarter(workspace, workers);

        // when, then
        assertThatThrownBy(() -> workspaceStarter.startBy(creator))
                .hasMessage(ErrorCode.ALREADY_ACTIVATED_WORKSPACE.getMessage());
    }

    @Test
    void 최소_인원을_만족하지_않는_경우_예외가_발생한다() {
        // given;
        Workspace workspace = getWorkspace("1234", WorkspaceStatus.PREPARING, WorkspaceCreationValidator.MIN_HEAD_COUNT);
        Worker creator = getWorker(workspace, workspace.getCreator());
        List<Worker> workers = List.of(creator);
        WorkspaceStarter workspaceStarter = new WorkspaceStarter(workspace, workers);

        // when, then
        assertThatThrownBy(() -> workspaceStarter.startBy(creator))
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
        WorkspaceStarter workspaceStarter = new WorkspaceStarter(workspace, workers);

        // when
        workspaceStarter.startBy(creator);

        // then
        assertThat(workspace.getStatus()).isEqualTo(WorkspaceStatus.IN_PROGRESS);
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
