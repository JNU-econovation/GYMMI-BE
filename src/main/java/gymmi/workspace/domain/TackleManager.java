package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.AlreadyExistException;
import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Tackle;
import gymmi.workspace.domain.entity.Vote;
import gymmi.workspace.domain.entity.Worker;
import lombok.Getter;

@Getter
public class TackleManager {
    private final Tackle tackle;

    public TackleManager(Tackle tackle) {
        this.tackle = tackle;
    }

    public Vote createVote(Worker worker, boolean isAgree) {
        if (!tackle.isOpen()) {
            throw new InvalidStateException(ErrorCode.ALREADY_CLOSED_TACKLE);
        }
        if (tackle.hasVoteBy(worker)) {
            throw new AlreadyExistException(ErrorCode.ALREADY_VOTED);
        }
        return new Vote(worker, tackle, isAgree);
    }

    public void closeIfOnMajorityOrDone(int headCount) {
        int majority = getMajority(headCount);
        if (tackle.getAgreeCount() >= majority || tackle.getDisAgreeCount() >= majority) {
            tackle.close();
            return;
        }
        if (tackle.getVoteCount() >= headCount) {
            tackle.close();
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
