package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.domain.entity.Vote;
import gymmi.workspace.service.VoteResult;

import java.util.List;

public class VoteReflector {

    private final Objection objection;
    private final int workerCount;
    private VoteResult voteResult;


    public VoteReflector(Objection objection, List<Vote> votes, int workerCount) {
        if (!votes.stream().allMatch(v -> v.isIn(objection))) {
            throw new IllegalArgumentException();
        }

        long distinctCount = votes.stream()
                .map(Vote::getObjection)
                .distinct()
                .count();
        if (distinctCount > 1) {
            throw new IllegalArgumentException("서로 다른 이의제기 ID가 섞여 있습니다.");
        }
        this.objection = objection;
        this.workerCount = workerCount;
        voteResult = new VoteResult(votes);
    }

    public void apply() {
        if (hasMajorityApproval(workerCount)) {
            objection.applyAndClose();
            return;
        }
        if (hasMajorityRejection(workerCount)) {
            objection.close();
            return;
        }
        return;
    }

    private int calculateMajority(int total) {
        return (total / 2) + 1;
    }

//    private int getMajority(int workerCount) {
//        if (workerCount % 2 == 0) {
//            return (workerCount / 2) + 1;
//        }
//        return (int) Math.round(workerCount / 2.0);
//    }

    private boolean hasMajorityApproval(int totalWorkerCount) {
        return voteResult.approvalCount() >= calculateMajority(totalWorkerCount);
    }

    private boolean hasMajorityRejection(int totalWorkerCount) {
        return voteResult.rejectionCount() >= calculateMajority(totalWorkerCount);
    }

}
