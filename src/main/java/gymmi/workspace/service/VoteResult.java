package gymmi.workspace.service;

import gymmi.workspace.domain.entity.Vote;

import java.util.List;

public record VoteResult(
        int approvalCount,
        int rejectionCount,
        int voteCount
) {

    public VoteResult(List<Vote> votes) {
        this(
                calculateApproval(votes),
                calculateRejection(votes),
                votes.size()
        );
    }

    private static int calculateApproval(List<Vote> votes) {
        return votes.stream()
                .filter(Vote::getIsApproved)
                .toList().size();
    }

    private static int calculateRejection(List<Vote> votes) {
        return votes.stream()
                .filter(vote -> !vote.getIsApproved())
                .toList().size();
    }

}
