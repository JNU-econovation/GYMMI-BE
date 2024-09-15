package gymmi.entity;

import gymmi.exception.class1.AlreadyExistException;
import gymmi.exception.InvalidStateException;
import gymmi.exception.NotMatchedException;
import gymmi.exception.message.ErrorCode;

import java.util.ArrayList;
import java.util.List;

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
            throw new InvalidStateException("참여자가 아닙니다.");
        }
        return new ArrayList<>(workers);
    }

    public Worker allow(User user, String password, String taskName) {
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

    public void start() {
        if (!workspace.isPreparing()) {
            throw new InvalidStateException("진행중이거나 이미 종료되었어요.");
        }
        if (workers.size() < 2) {
            throw new InvalidStateException("최소 인원인 2명을 채워주세요.");
        }
        workspace.changeStatusTo(WorkspaceStatus.IN_PROGRESS);
    }


}
