package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.service.S3Service;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.response.ObjectionResponse;
import gymmi.workspace.response.WorkoutConfirmationDetailResponse;
import gymmi.workspace.response.WorkoutConfirmationResponse;
import gymmi.workspace.response.WorkoutContextResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class WorkspaceQueryServiceTest extends IntegrationTest {

    @Autowired
    WorkspaceQueryService workspaceQueryService;

    @MockBean
    S3Service s3Service;

    @Test
    void 참여자의_운동_현황을_확인_한다() {
        // given
        User creator = persister.persistUser();
        User user = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS, 100, 2);
        Mission mission = persister.persistMission(workspace, 1);
        Mission mission1 = persister.persistMission(workspace, 5);
        Worker workerCreator = persister.persistWorker(creator, workspace);
        Worker worker = persister.persistWorker(user, workspace);

        persister.persistWorkoutHistoryAndApply(workerCreator, Map.of(mission, 1, mission1, 4));
        persister.persistWorkoutHistoryAndApply(worker, Map.of(mission, 2, mission1, 2));
        persister.persistWorkoutHistoryAndApply(worker, Map.of(mission, 1, mission1, 1));

        // when
        WorkoutContextResponse response = workspaceQueryService.getWorkoutContext(
                creator,
                workspace.getId(),
                user.getId()
        );

        // then
        assertThat(response.getBestDailyScore()).isEqualTo(2 + 10);
        assertThat(response.getGabScoreFromFirst()).isEqualTo(21 - (12 + 6));
        assertThat(response.getTotalContributedScore()).isEqualTo(12 + 6);
        assertThat(response.getTotalWorkoutCount()).isEqualTo(2);
    }

    @Test
    void 워크스페이스의_운동_인증_목록을_확인_한다() {
        // given
        User creator = persister.persistUser();
        User user = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS);
        Worker creatorWorker = persister.persistWorker(creator, workspace);
        Worker userWorker = persister.persistWorker(user, workspace);
        Mission mission = persister.persistMission(workspace, 1);
        Mission mission1 = persister.persistMission(workspace, 5);
        WorkoutConfirmation workoutConfirmation = new WorkoutConfirmation("creator", "a");
        WorkoutConfirmation workoutConfirmation1 = new WorkoutConfirmation("user", "b");

        WorkoutHistory workoutHistory = persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 2, mission1, 2), workoutConfirmation);
        WorkoutHistory workoutHistory1 = persister.persistWorkoutHistoryAndApply(userWorker, Map.of(mission, 2, mission1, 2), workoutConfirmation1);

        // when
        List<WorkoutConfirmationResponse> responses = workspaceQueryService.getWorkoutConfirmations(user, workspace.getId(), 0);

        // then
        assertThat(responses).hasSize(2);
        WorkoutConfirmationResponse response = responses.get(0);
        assertThat(response.getNickname()).isEqualTo(user.getNickname());
        assertThat(response.getProfileImageUrl()).isEqualTo(user.getProfileImageName());
        assertThat(response.getWorkoutConfirmationId()).isEqualTo(workoutHistory1.getWorkoutConfirmation().getId());
        WorkoutConfirmationResponse response1 = responses.get(1);
        assertThat(response1.getNickname()).isEqualTo(creator.getNickname());
        assertThat(response1.getProfileImageUrl()).isEqualTo(creator.getProfileImageName());
        assertThat(response1.getWorkoutConfirmationId()).isEqualTo(workoutHistory.getWorkoutConfirmation().getId());
    }

    @Test
    void 운동_인증_상세를_확인_한다() {
        // given
        User creator = persister.persistUser();
        User user = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS);
        Worker creatorWorker = persister.persistWorker(creator, workspace);
        Worker userWorker = persister.persistWorker(user, workspace);
        Mission mission = persister.persistMission(workspace, 1);
        Mission mission1 = persister.persistMission(workspace, 5);
        WorkoutConfirmation workoutConfirmation = new WorkoutConfirmation("creator", "a");
        WorkoutHistory workoutHistory = persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 2, mission1, 2), workoutConfirmation);

        // when
        WorkoutConfirmationDetailResponse response = workspaceQueryService.getWorkoutConfirmation(user, workspace.getId(), workoutConfirmation.getId());

        // then
        assertThat(response.getComment()).isEqualTo("a");
        assertThat(response.getProfileImageUrl()).isEqualTo(creator.getProfileImageName());
        assertThat(response.getLoginId()).isEqualTo(creator.getLoginId());
        assertThat(response.getNickname()).isEqualTo(creator.getNickname());
        assertThat(response.getObjectionId()).isEqualTo(null);
    }

    @Nested
    class 이의_신청_확인 {

        @Test
        void 투표_o_마감_x() {
            // given
            User creator = persister.persistUser();
            User user = persister.persistUser();
            Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS, 100, 5);
            Worker creatorWorker = persister.persistWorker(creator, workspace);
            Worker userWorker = persister.persistWorker(user, workspace);
            WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
            Objection objection = persister.persistObjection(userWorker, true, workoutConfirmation);
            persister.persistVote(userWorker, objection, true);
            persister.persistVote(creatorWorker, objection, false);

            // when
            ObjectionResponse response = workspaceQueryService.getObjection(user, workspace.getId(), objection.getId());

            // then
            assertThat(response.getDeadline()).isEqualTo(objection.getCreatedAt().plusHours(24));
            assertThat(response.getInInProgress()).isTrue();
            assertThat(response.getVoteCompletion()).isTrue();
            assertThat(response.getApprovalCount()).isEqualTo(1);
            assertThat(response.getRejectionCount()).isEqualTo(1);
            assertThat(response.getVoteParticipationCount()).isEqualTo(2);
            assertThat(response.getConfirmationCompletion()).isNull();
        }

        @Test
        void 투표_x_마감_x() {
            // given
            User creator = persister.persistUser();
            User user = persister.persistUser();
            Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS, 100, 5);
            Worker creatorWorker = persister.persistWorker(creator, workspace);
            Worker userWorker = persister.persistWorker(user, workspace);
            WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
            Objection objection = persister.persistObjection(userWorker, true, workoutConfirmation);
            persister.persistVote(creatorWorker, objection, false);

            // when
            ObjectionResponse response = workspaceQueryService.getObjection(user, workspace.getId(), objection.getId());

            // then
            assertThat(response.getDeadline()).isEqualTo(objection.getCreatedAt().plusHours(24));
            assertThat(response.getInInProgress()).isTrue();
            assertThat(response.getVoteCompletion()).isFalse();
            assertThat(response.getApprovalCount()).isNull();
            assertThat(response.getRejectionCount()).isNull();
            assertThat(response.getVoteParticipationCount()).isEqualTo(1);
            assertThat(response.getConfirmationCompletion()).isNull();
        }

        @Test
        void 투표_x_마감_o() {
            // given
            User creator = persister.persistUser();
            User user = persister.persistUser();
            Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS, 100, 3);
            Worker creatorWorker = persister.persistWorker(creator, workspace);
            Worker userWorker = persister.persistWorker(user, workspace);
            Mission mission = persister.persistMission(workspace, 10);
            WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
            persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 1), workoutConfirmation);
            Objection objection = persister.persistObjection(userWorker, false, workoutConfirmation);
            persister.persistVote(creatorWorker, objection, false);
            ;

            // when
            ObjectionResponse response = workspaceQueryService.getObjection(user, workspace.getId(), objection.getId());

            // then
            assertThat(response.getDeadline()).isEqualTo(objection.getCreatedAt().plusHours(24));
            assertThat(response.getInInProgress()).isFalse();
            assertThat(response.getVoteCompletion()).isFalse();
            assertThat(response.getApprovalCount()).isEqualTo(0);
            assertThat(response.getRejectionCount()).isEqualTo(1);
            assertThat(response.getVoteParticipationCount()).isEqualTo(1);
            assertThat(response.getConfirmationCompletion()).isTrue();
        }

    }
}
