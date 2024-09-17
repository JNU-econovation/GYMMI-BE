package gymmi.workspace.domain;

import static gymmi.exception.message.ErrorCode.ALREADY_ACTIVATED_WORKSPACE;
import static gymmi.exception.message.ErrorCode.BELOW_MINIMUM_WORKER;
import static gymmi.exception.message.ErrorCode.EXIST_WORKERS_EXCLUDE_CREATOR;
import static gymmi.exception.message.ErrorCode.FULL_WORKSPACE;
import static gymmi.exception.message.ErrorCode.NOT_JOINED_WORKSPACE;
import static gymmi.exception.message.ErrorCode.NOT_MATCHED_PASSWORD;
import static gymmi.exception.message.ErrorCode.NOT_WORKSPACE_CREATOR;

import gymmi.entity.User;
import gymmi.exception.class1.AlreadyExistException;
import gymmi.exception.class1.InvalidStateException;
import gymmi.exception.class1.NotHavePermissionException;
import gymmi.exception.class1.NotMatchedException;
import gymmi.exception.message.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class WorkspacePreparingManager {

    private final Workspace workspace;
    private final List<Worker> workers;

    public WorkspacePreparingManager(Workspace workspace, List<Worker> workers) {
        WorkspaceWithWorkersConsistencyValidator.validateWorkersConsistency(workspace, workers);
        this.workspace = workspace;
        this.workers = new ArrayList<>(workers);
    }

    public Worker allow(User user, String password, String taskName) {
        if (!workspace.matchesPassword(password)) {
            throw new NotMatchedException(NOT_MATCHED_PASSWORD);
        }
        if (workers.size() >= workspace.getHeadCount()) {
            throw new InvalidStateException(FULL_WORKSPACE);
        }
        if (!workspace.isPreparing()) {
            throw new InvalidStateException(ALREADY_ACTIVATED_WORKSPACE);
        }
        if (workers.stream()
                .anyMatch(worker -> worker.getUser().equals(user))) {
            throw new AlreadyExistException(ErrorCode.ALREADY_JOINED_WORKSPACE);
        }

        Worker worker = createWorker(user, taskName);
        workers.add(worker);
        return worker;
    }

    private Worker createWorker(User user, String taskName) {
        Task task = new Task(taskName);
        Worker worker = new Worker(user, workspace, task);
        return worker;
    }

    public WorkerLeavedEvent release(Worker worker) {
        if (!worker.isJoinedIn(workspace)) {
            throw new InvalidStateException(NOT_JOINED_WORKSPACE);
        }
        if (!workspace.isPreparing()) {
            throw new InvalidStateException(ALREADY_ACTIVATED_WORKSPACE);
        }
        if (workspace.isCreatedBy(worker.getUser())) {
            if (workers.size() != 1) {
                throw new InvalidStateException(EXIST_WORKERS_EXCLUDE_CREATOR);
            }
        }
        workers.remove(worker);
        return new WorkerLeavedEvent(worker, workers.size() == 0);
    }

    public void startBy(Worker creator) {
        if (!workspace.isCreatedBy(creator)) {
            throw new NotHavePermissionException(NOT_WORKSPACE_CREATOR);
        }
        if (!workspace.isPreparing()) {
            throw new InvalidStateException(ALREADY_ACTIVATED_WORKSPACE);
        }
        if (workers.size() < 2) {
            throw new InvalidStateException(BELOW_MINIMUM_WORKER);
        }
        workspace.changeStatusTo(WorkspaceStatus.IN_PROGRESS);
    }

}
