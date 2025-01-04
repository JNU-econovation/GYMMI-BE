package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.AlreadyExistException;
import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.domain.entity.Vote;
import gymmi.workspace.domain.entity.Worker;
import lombok.Getter;

import java.util.List;

@Getter
public class ObjectionManager {
    private final Objection objection;
    private boolean isApproved;

    public ObjectionManager(Objection objection) {
        this.objection = objection;
        this.isApproved = false;
    }

    public Vote createVote(Worker worker, boolean isApproved) {
        if (!objection.isInProgress()) {
            throw new InvalidStateException(ErrorCode.ALREADY_CLOSED_OBJECTION);
        }
        if (objection.hasVoteBy(worker)) {
            throw new AlreadyExistException(ErrorCode.ALREADY_VOTED);
        }
        return new Vote(worker, objection, isApproved);
    }

    public boolean closeIfOnMajorityOrDone(int workerCount) {
        int majority = getMajority(workerCount);
        if (objection.getApprovalCount() >= majority) {
            objection.close();
            isApproved = true;
            return true;
        }
        if (objection.getRejectionCount() >= majority) {
            objection.close();
            return true;
        }
        if (objection.getVoteCount() >= workerCount) {
            objection.close();
            return true;
        }
        return false;
    }


    private int getMajority(int workerCount) {
        if (workerCount % 2 == 0) {
            return (workerCount / 2) + 1;
        }
        return (int) Math.round(workerCount / 2.0);
    }

    public List<Vote> createAutoVote(List<Worker> workers) {
        return workers.stream()
                .filter(worker -> !objection.hasVoteBy(worker))
                .map(worker -> Vote.autoVote(worker, objection))
                .toList();
    }

}
