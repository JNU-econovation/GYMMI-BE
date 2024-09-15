package gymmi.workspace.domain;

import static gymmi.exception.message.ErrorCode.EXIST_NOT_JOINED_WORKER;
import static gymmi.exception.message.ErrorCode.NOT_JOINED_WORKSPACE;

import gymmi.exception.class1.InvalidStateException;
import gymmi.exception.message.ErrorCode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WorkspaceDrawManager {

    private final Workspace workspace;
    private final List<Worker> workers;

    public WorkspaceDrawManager(Workspace workspace, List<Worker> workers) {
        this.workspace = validateStatus(workspace);
        this.workers = validate(workers);
    }

    private List<Worker> validate(List<Worker> workers) {
        if (!workers.stream()
                .allMatch(worker -> worker.isJoinedIn(workspace))) {
            throw new InvalidStateException(EXIST_NOT_JOINED_WORKER);
        }
        return new ArrayList<>(workers);
    }

    private Workspace validateStatus(Workspace workspace) {
        if (!workspace.isCompleted()) {
            throw new InvalidStateException(ErrorCode.NOT_REACHED_WORKSPACE_GOAL_SCORE);
        }
        return workspace;
    }

    public void drawIfNotPicked() {
        if (workspace.isFullyCompleted()) {
            return;
        }
        Collections.sort(workers, Comparator.comparing(Worker::getContributedScore).reversed());
        Task task = workers.get(0).getTask();
        task.changeToPicked();
        workspace.changeStatusTo(WorkspaceStatus.FULLY_COMPLETED);
    }

    public List<Task> getTasks(Worker worker) {
        if (worker.isJoinedIn(workspace)) {
            throw new InvalidStateException(NOT_JOINED_WORKSPACE);
        }
        return workers.stream()
                .map(Worker::getTask)
                .toList();
    }

}
