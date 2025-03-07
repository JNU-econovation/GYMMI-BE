package gymmi.workspace.domain;

import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
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
        if (!workspace.isPreparing()) {
            return false;
        }
        return workspace.getHeadCount() > workers.size();
    }

    public boolean canEnter(Worker worker) {
        if (worker == null) {
            return false;
        }
        return workers.contains(worker);
    }

}
