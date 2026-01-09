package gymmi.workspace.vote.domain;

import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.vote.domain.entity.Vote;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import lombok.Getter;

@Getter
public class VoteManger {

    public Vote createVote(Workspace workspace, Objection objection, Worker worker, boolean isApproved, VoteValidator validator) {
        validator.validateCanVote(workspace, worker, objection);
        return new Vote(worker, objection, isApproved);
    }


}
