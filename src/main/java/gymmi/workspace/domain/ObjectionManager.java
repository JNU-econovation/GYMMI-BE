package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.AlreadyExistException;
import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.domain.entity.Vote;
import gymmi.workspace.domain.entity.Worker;
import lombok.Getter;

@Getter
public class ObjectionManager {
    private final Objection objection;

    public ObjectionManager(Objection objection) {
        this.objection = objection;
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

    public void closeIfOnMajorityOrDone(int headCount) {
        int majority = getMajority(headCount);
        if (objection.getAgreeCount() >= majority || objection.getDisAgreeCount() >= majority) {
            objection.close();
            return;
        }
        if (objection.getVoteCount() >= headCount) {
            objection.close();
        }
    }

    private int getMajority(int headCount) {
        if (headCount % 2 == 0) {
            return (headCount / 2) + 1;
        }
        if (headCount % 2 == 1) {
            return (int) Math.round(headCount / 2.0);
        }
        throw new IllegalStateException();
    }
}
