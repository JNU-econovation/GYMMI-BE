package gymmi.entity;

import gymmi.exception.class1.InvalidStateException;

import java.util.ArrayList;
import java.util.List;

import static gymmi.exception.message.ErrorCode.EXIST_NOT_JOINED_WORKER;

public class WorkspaceGateChecker {

    private final Workspace workspace;
    private final List<Worker> workers;

    public WorkspaceGateChecker(Workspace workspace, List<Worker> workers) {
        this.workspace = workspace;
        this.workers = validate(workers);
    }

    private List<Worker> validate(List<Worker> workers) {
        if (!workers.stream()
                .allMatch(worker -> worker.isJoinedIn(workspace))) {
            throw new InvalidStateException(EXIST_NOT_JOINED_WORKER);
        }
        return new ArrayList<>(workers);
    }

    public boolean canJoin() {
        return workers.size() >= workspace.getHeadCount();
    }

    public boolean canEnter(Worker worker) {
        if (worker == null) {
            return false;
        }
        return workers.contains(worker);
    }

}
