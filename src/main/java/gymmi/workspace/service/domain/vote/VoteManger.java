package gymmi.workspace.service.domain.vote;

import gymmi.workspace.service.domain.objection.Objection;
import gymmi.workspace.service.domain.workspace.Worker;
import gymmi.workspace.service.domain.workspace.Workspace;
import lombok.Getter;

@Getter
public class VoteManger {

    public Vote createVote(Workspace workspace, Objection objection, Worker worker, boolean isApproved, VoteValidator validator) {
        validator.validateCanVote(workspace, worker, objection);
        return new Vote(worker, objection, isApproved);
    }


}
