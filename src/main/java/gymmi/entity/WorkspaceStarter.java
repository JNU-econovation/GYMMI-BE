package gymmi.entity;

import gymmi.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceStarter {

    private final Workspace workspace;
    private final List<Worker> workers;

    public WorkspaceStarter(Workspace workspace, List<Worker> workers) {
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

    public void start() {
        if (workers.size() < 2) {
            throw new InvalidStateException("최소 인원인 2명을 채워주세요.");
        }
        workspace.changeStatusTo(WorkspaceStatus.IN_PROGRESS);
    }

}
