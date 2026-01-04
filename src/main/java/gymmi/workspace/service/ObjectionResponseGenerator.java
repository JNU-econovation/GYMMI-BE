package gymmi.workspace.service;

import gymmi.workspace.domain.entity.*;
import gymmi.workspace.response.ObjectionResponse;

import java.util.List;

public class ObjectionResponseGenerator {

    private final Workspace workspace;
    private final Worker worker;
    private final Objection objection;
    private final List<Vote> votes;
    private final WorkoutHistory workoutHistory;

    public ObjectionResponseGenerator(Workspace workspace, Worker worker, Objection objection, List<Vote> votes, WorkoutHistory workoutHistory) {
        this.workspace = workspace;
        this.worker = worker;
        this.objection = objection;
        this.votes = votes;
        this.workoutHistory = workoutHistory;
    }

    public ObjectionResponse generate() {
        ObjectionWithVotes objectionWithVotes = new ObjectionWithVotes(objection, votes);
        if (objection.isInProgress() && hasVoted()) {
            return ObjectionResponse.objectionInProgressWithVoteCompletion(objectionWithVotes, workspace.getHeadCount());
        }

        if (objection.isInProgress() && !hasVoted()) {
            return ObjectionResponse.objectionInProgressWithVoteInCompletion(objectionWithVotes, workspace.getHeadCount());
        }

        return ObjectionResponse.closedObjection(objectionWithVotes, objection.hasVoteBy(worker), workoutHistory.isRejected(), workspace.getHeadCount());
    }

    private boolean hasVoted() {
        return votes.stream().anyMatch(vote -> vote.isVotedBy(worker));
    }

}

