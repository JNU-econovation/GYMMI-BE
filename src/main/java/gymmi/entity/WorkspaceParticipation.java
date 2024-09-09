package gymmi.entity;

import gymmi.exception.AlreadyExistException;
import gymmi.exception.InvalidStateException;
import gymmi.exception.NotMatchedException;

import java.util.List;

public class WorkspaceParticipation {

    private final Workspace workspace;
    private final List<Worker> workers;

    public WorkspaceParticipation(Workspace workspace, List<Worker> workers) {
        this.workspace = workspace;
        this.workers = workers;
    }

    private void validate() {

    }

    public Worker join(User user, String password, String taskName) {
        if (workspace.matchesPassword(password)) {
            throw new NotMatchedException("비밀번호가 일치하지 않습니다.");
        }
        if (!workspace.isPreparing()) {
            throw new InvalidStateException("준비중인 워크스페이스에만 참여할 수 있습니다.");
        }
        if (workers.size() >= workspace.getHeadCount()) {
            throw new InvalidStateException("워크스페이스 인원이 가득 찼습니다.");
        }
        if (workers.stream()
                .filter(w -> w.getUser().equals(user))
                .findAny().isPresent()) {
            throw new AlreadyExistException("이미 참여한 워크스페이스 입니다.");
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

}
