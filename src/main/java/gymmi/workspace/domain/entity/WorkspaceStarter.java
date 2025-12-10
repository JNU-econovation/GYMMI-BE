package gymmi.workspace.domain.entity;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import gymmi.workspace.domain.WorkspaceStatus;

import java.util.List;

import static gymmi.exceptionhandler.message.ErrorCode.*;

public class WorkspaceStarter {

    public static final int MINIMUM_STARTING_WORKERS_COUNT = 2;

    private final Workspace workspace;
    private final List<Worker> workers;

    public WorkspaceStarter(Workspace workspace, List<Worker> workers) {
        this.workspace = workspace;
        this.workers = workers;
    }

    public Workspace startBy(Worker worker) {
        validateCanStart(worker);
        workspace.changeStatusTo(WorkspaceStatus.IN_PROGRESS);
        return workspace;
    }

    private void validateCanStart(Worker worker) {
        if (!workspace.isPreparing()) {
            throw new InvalidStateException(ALREADY_ACTIVATED_WORKSPACE);
        }
        if (!worker.isCreator(workspace)) {
            throw new NotHavePermissionException(NOT_WORKSPACE_CREATOR);
        }
        if (!hasReachedMinimumHeadCount()) {
            throw new InvalidStateException(BELOW_MINIMUM_WORKER);
        }
    }

    private boolean hasReachedMinimumHeadCount() {
        return workers.size() >= MINIMUM_STARTING_WORKERS_COUNT;
    }
}
