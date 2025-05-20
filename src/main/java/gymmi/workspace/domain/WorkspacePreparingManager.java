package gymmi.workspace.domain;


import gymmi.entity.User;
import gymmi.exceptionhandler.exception.AlreadyExistException;
import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import gymmi.exceptionhandler.exception.NotMatchedException;
import gymmi.exceptionhandler.message.WorkspaceErrorMessage;
import gymmi.exceptionhandler.message.WorkspaceJoinErrorMessage;
import gymmi.exceptionhandler.message.WorkspaceStartErrorMessage;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static gymmi.exceptionhandler.message.WorkspaceErrorMessage.*;
import static gymmi.exceptionhandler.message.WorkspaceJoinErrorMessage.ALREADY_ACTIVATED_WORKSPACE;

@Getter
public class WorkspacePreparingManager {

    private final Workspace workspace;
    private final List<Worker> workers;

    public WorkspacePreparingManager(Workspace workspace, List<Worker> workers) {
        WorkspaceWithWorkersConsistencyValidator.validateWorkersConsistency(workspace, workers);
        this.workspace = workspace;
        this.workers = new ArrayList<>(workers);
    }

    public Worker allow(User user, String password) {
        if (!workspace.matchesPassword(password)) {
            throw new NotMatchedException(WorkspaceJoinErrorMessage.NOT_MATCHED_PASSWORD.getMessage());
        }
        if (workers.size() >= workspace.getHeadCount()) {
            throw new InvalidStateException(WorkspaceJoinErrorMessage.FULL_WORKSPACE.getMessage());
        }
        if (!workspace.isPreparing()) {
            throw new InvalidStateException(ALREADY_ACTIVATED_WORKSPACE.getMessage());
        }
        if (workers.stream()
                .anyMatch(worker -> worker.getUser().equals(user))) {
            throw new AlreadyExistException(WorkspaceJoinErrorMessage.ALREADY_JOINED_WORKSPACE.getMessage());
        }

        Worker worker = new Worker(user, workspace);
        workers.add(worker);
        return worker;
    }

    public WorkerLeavedEvent release(Worker worker) {
        if (!worker.isJoinedIn(workspace)) {
            throw new InvalidStateException(NOT_JOINED_WORKSPACE.getMessage());
        }
        if (!workspace.isPreparing()) {
            throw new InvalidStateException(ALREADY_ACTIVATED_WORKSPACE.getMessage());
        }
        if (workspace.isCreatedBy(worker.getUser())) {
            if (workers.size() != 1) {
                throw new InvalidStateException(EXIST_WORKERS_EXCLUDE_CREATOR.getMessage());
            }
        }
        workers.remove(worker);
        return new WorkerLeavedEvent(worker, workers.size() == 0);
    }

    public void startBy(Worker creator) {
        if (!workspace.isCreatedBy(creator)) {
            throw new NotHavePermissionException(WorkspaceStartErrorMessage.NOT_WORKSPACE_CREATOR.getMessage());
        }
        if (!workspace.isPreparing()) {
            throw new InvalidStateException(WorkspaceStartErrorMessage.ALREADY_ACTIVATED_WORKSPACE.getMessage());
        }
        if (workers.size() < Workspace.MIN_HEAD_COUNT) {
            throw new InvalidStateException(WorkspaceStartErrorMessage.BELOW_MINIMUM_WORKER.getMessage());
        }
        workspace.changeStatusTo(WorkspaceStatus.IN_PROGRESS);
    }

}
