package gymmi.workspace.workspace.domain;

import gymmi.user.domain.User;
import gymmi.global.exception.exceptiontype.AlreadyExistException;
import gymmi.global.exception.exceptiontype.InvalidStateException;
import gymmi.global.exception.exceptiontype.NotMatchedException;
import gymmi.global.exception.message.ErrorCode;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static gymmi.global.exception.message.ErrorCode.*;

@Getter
public class WorkspacePreparingManager {

    private final Workspace workspace;
    private final List<Worker> workers;

    public WorkspacePreparingManager(Workspace workspace, List<Worker> workers) {
        requirePreparingStatus(workspace);
        this.workspace = workspace;
        this.workers = new ArrayList<>(workers);
    }

    public Worker allow(User user, String workspacePassword) {
        validateJoinable(user, workspacePassword);
        Worker worker = new Worker(user, workspace);
        workers.add(worker);
        return worker;
    }

    public LeftWorker release(Worker worker) {
        validateCanLeave(worker);
        workers.remove(worker);
        return new LeftWorker(worker, workers.isEmpty());
    }

    private void validateCanLeave(Worker worker) {
        if (!worker.isJoinedIn(workspace)) {
            throw new InvalidStateException(NOT_JOINED_WORKSPACE);
        }
        if (worker.isCreator(workspace) && !hasSingleParticipant()) {
            throw new InvalidStateException(EXIST_WORKERS_EXCLUDE_CREATOR);
        }
    }

    private boolean hasSingleParticipant() {
        return workers.size() == 1;
    }

    private void requirePreparingStatus(Workspace workspace) {
        if (!workspace.isPreparing()) {
            throw new InvalidStateException(ALREADY_ACTIVATED_WORKSPACE);
        }
    }

    private void validateJoinable(User user, String workspacePassword) {
        if (!workspace.matchesPassword(workspacePassword)) {
            throw new NotMatchedException(NOT_MATCHED_PASSWORD);
        }
        if (hasReachedHeadCount()) {
            throw new InvalidStateException(FULL_WORKSPACE);
        }
        if (hasParticipant(user)) {
            throw new AlreadyExistException(ErrorCode.ALREADY_JOINED_WORKSPACE);
        }
    }

    private boolean hasParticipant(User user) {
        return workers.stream().anyMatch(worker -> worker.matches(user));
    }

    private boolean hasReachedHeadCount() {
        return workers.size() >= workspace.getHeadCount();
    }

}
