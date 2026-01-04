package gymmi.workspace.domain;

import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.domain.entity.Vote;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import lombok.Getter;

import java.util.List;

@Getter
public class VoteManger {

    public Vote createVote(Workspace workspace, Objection objection, Worker worker, boolean isApproved, VoteValidator validator) {
        validator.validateCanVote(workspace, worker, objection);
        return new Vote(worker, objection, isApproved);
    }


}
