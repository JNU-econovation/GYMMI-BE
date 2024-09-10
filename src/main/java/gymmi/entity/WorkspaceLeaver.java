package gymmi.entity;

import gymmi.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceLeaver {

    private final Workspace workspace;
    private final List<Worker> workers;

    public WorkspaceLeaver(Workspace workspace, List<Worker> workers) {
        this.workspace = workspace;
        this.workers = validate(workers);
    }

    private List<Worker> validate(List<Worker> workers) {
        if (!workers.stream()
                .allMatch(worker -> worker.isJoinedIn(workspace))) {
            throw new InvalidStateException("참여자가 아닙니다.");
        }
        return new ArrayList<>(workers);
    }

    public WorkerLeavedEvent detach(Worker worker) {
        if (!workspace.isPreparing()) {
            throw new InvalidStateException("준비 단계에서만 나갈 수 있습니다.");
        }
        if (workspace.isCreatedBy(worker.getUser())) {
            if (workers.size() != 1) {
                throw new InvalidStateException("방장 이외에 참여자가 존재합니다.");
            }
        }
        workers.remove(worker);
        return new WorkerLeavedEvent(worker, workers.size() == 0);
    }

}
