package gymmi.workspace.service;

import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.domain.entity.Vote;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class ObjectionWithVotes {

    private final Objection objection;
    private final List<Vote> votes;

    public ObjectionWithVotes(Objection objection, List<Vote> votes) {
        this.objection = objection;
        this.votes = new ArrayList<>(votes);
    }

    public int getApprovalCount() {
        return votes.stream()
                .filter(Vote::getIsApproved)
                .toList().size();
    }

    public int getVoteCount() {
        return votes.size();
    }

    public int getRejectionCount() {
        return votes.stream()
                .filter(vote -> !vote.getIsApproved())
                .toList().size();
    }

    public Objection getObjection() {
        return objection;
    }

    public List<Vote> getVotes() {
        return votes;
    }
}
