package gymmi.workspace.domain;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceGateChecker {

    private final Workspace workspace;
    private final List<Worker> workers;

    public WorkspaceGateChecker(Workspace workspace, List<Worker> workers) {
        WorkspaceWithWorkersConsistencyValidator.validateWorkersConsistency(workspace, workers);
        this.workspace = workspace;
        this.workers = new ArrayList<>(workers);
    }

    public boolean canJoin() {
        return workspace.getHeadCount() > workers.size();
    }

    public boolean canEnter(Worker worker) {
        if (worker == null) {
            return false;
        }
        return workers.contains(worker);
    }

}
