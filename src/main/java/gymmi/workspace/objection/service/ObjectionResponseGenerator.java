package gymmi.workspace.objection.service;

import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.vote.domain.entity.Vote;
import gymmi.workspace.vote.domain.VoteResult;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.objection.controller.response.ObjectionResponse;

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

