package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.domain.entity.WorkspaceResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WorkspaceDrawManger {

    private final List<Worker> workers;
    private final Workspace workspace;

    public WorkspaceDrawManger(Workspace workspace, List<Worker> workers) {
        if (!workspace.isCompleted()) {
            throw new InvalidStateException(ErrorCode.NOT_COMPLETED_WORKSPACE);
        }
        this.workers = new ArrayList<>(workers);
        this.workspace = workspace;
        workers.sort(Comparator.comparing(Worker::getContributedScore).reversed());
    }

    public WorkspaceResult draw() {
        Worker first = workers.get(0);
        Worker last = workers.get(workers.size() - 1);
        Worker winner = pickOneInTie(first);
        Worker loser = pickOneInTie(last);
        WorkspaceResult workspaceResult = new WorkspaceResult(workspace, winner, loser);
        workspace.changeStatusTo(WorkspaceStatus.FULLY_COMPLETED);
        return workspaceResult;
    }

    private Worker pickOneInTie(Worker worker) {
        List<Worker> tieWorkers = workers.stream()
                .filter(w -> w.isTie(worker))
                .collect(Collectors.toList());
        Collections.shuffle(tieWorkers);
        return tieWorkers.get(0);
    }

}
