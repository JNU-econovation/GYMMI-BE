package gymmi.workspace.vote.domain;

import gymmi.etc.domain.entity.User;
import gymmi.fixture.*;
import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.vote.domain.entity.Vote;
import gymmi.workspace.workout.domain.entity.WorkoutConfirmation;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import org.junit.jupiter.api.Test;

import java.util.List;

class VoteReflectorTest {


//    @Test
//    void 이의신청_투표를_통해_이의신청이_찬성되어_점수가_몰수된다() {
//        // given
//        User creator = persister.persistUser();
//        User user = persister.persistUser();
//        User user1 = persister.persistUser();
//        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS, 100, 3);
//        Worker creatorWorker = persister.persistWorker(creator, workspace);
//        Worker userWorker = persister.persistWorker(user, workspace);
//        Worker user1Worker = persister.persistWorker(user1, workspace);
//        WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
//        Mission mission = persister.persistMission(workspace, 10);
//        persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 1), workoutConfirmation);
//        Objection objection = persister.persistObjection(userWorker, true, workoutConfirmation);
//        persister.persistVote(userWorker, objection, true);
//        persister.persistVote(creatorWorker, objection, false);
//        VoteRequest request = new VoteRequest(true);
//
//        // when
//        voteService.voteToObjection(user1, workspace.getId(), objection.getId(), request.getWillApprove());
//
//        // then
//        WorkoutHistory workoutHistory = workoutHistoryRepository.findByWorkoutConfirmationIdOrThrow(workoutConfirmation.getId());
//        assertThat(voteRepository.findAll().size()).isEqualTo(3);
//        assertThat(objection.isInProgress()).isEqualTo(false);
//        assertThat(workoutHistory.isRejected()).isFalse();
//        assertThat(userWorker.getContributedScore()).isEqualTo(0);
//    }

}
