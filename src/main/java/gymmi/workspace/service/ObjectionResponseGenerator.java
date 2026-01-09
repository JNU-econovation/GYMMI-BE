package gymmi.workspace.service;

import gymmi.workspace.service.domain.objection.Objection;
import gymmi.workspace.service.domain.vote.Vote;
import gymmi.workspace.service.domain.vote.VoteResult;
import gymmi.workspace.service.domain.workspace.Worker;
import gymmi.workspace.service.domain.workout.WorkoutHistory;
import gymmi.workspace.service.domain.workspace.Workspace;
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
        if (objection.isInProgress() && hasVoted()) {
            return ObjectionResponse.objectionInProgressWithVoteCompletion(objection, new VoteResult(votes), workspace.getHeadCount());
        }

        if (objection.isInProgress() && !hasVoted()) {
            return ObjectionResponse.objectionInProgressWithVoteInCompletion(objection, new VoteResult(votes), workspace.getHeadCount());
        }

        return ObjectionResponse.closedObjection(objection, new VoteResult(votes), objection.hasVoteBy(worker), workoutHistory.isRejected(), workspace.getHeadCount());
    }

    private boolean hasVoted() {
        return votes.stream().anyMatch(vote -> vote.isVotedBy(worker));
    }

}

