package gymmi.entity;

import gymmi.exception.class1.AlreadyExistException;
import gymmi.exception.class1.InvalidStateException;
import gymmi.exception.class1.NotMatchedException;
import gymmi.exception.message.ErrorCode;

import java.util.ArrayList;
import java.util.List;

import static gymmi.exception.message.ErrorCode.*;

public class WorkspacePreparingManager {

    private final Workspace workspace;
    private final List<Worker> workers;

    public WorkspacePreparingManager(Workspace workspace, List<Worker> workers) {
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

    public void start() {
        if (!workspace.isPreparing()) {
            throw new InvalidStateException(ALREADY_ACTIVATED_WORKSPACE);
        }
        if (workers.size() < 2) {
            throw new InvalidStateException(BELOW_MINIMUM_WORKER);
        }
        workspace.changeStatusTo(WorkspaceStatus.IN_PROGRESS);
    }


}
