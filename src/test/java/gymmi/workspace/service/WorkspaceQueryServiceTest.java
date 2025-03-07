package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.service.S3Service;
import gymmi.workspace.domain.ObjectionStatus;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.repository.WorkoutHistoryRepository;
import gymmi.workspace.response.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


class WorkspaceQueryServiceTest extends IntegrationTest {


    @Autowired
    WorkspaceQueryService workspaceQueryService;

    @Autowired
    WorkoutHistoryRepository workoutHistoryRepository;

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
        Objection objection = persister.persistObjection(userWorker, true, workoutConfirmation);
        WorkoutHistory workoutHistory1 = persister.persistWorkoutHistoryAndApply(userWorker, Map.of(mission, 2, mission1, 2), workoutConfirmation1);
        persister.persistVote(userWorker, objection, false);

        // when
        WorkoutConfirmationResponse workoutConfirmationResponse = workspaceQueryService.getWorkoutConfirmations(user, workspace.getId(), 0);

        // then
        List<WorkoutConfirmationOrObjectionResponse> responses = workoutConfirmationResponse.getData();
        assertThat(responses).hasSize(3);
        assertThat(workoutConfirmationResponse.getVoteIncompletionCount()).isEqualTo(0);

        WorkoutConfirmationOrObjectionResponse response = responses.get(0);
        assertThat(response.getIsMine()).isEqualTo(false);
        assertThat(response.getProfileImageUrl()).isEqualTo(creator.getProfileImageName());
        assertThat(response.getWorkoutConfirmationId()).isEqualTo(workoutHistory.getWorkoutConfirmation().getId());
        assertThat(response.getIsObjection()).isEqualTo(false);
        assertThat(response.getObjectionId()).isEqualTo(objection.getId());

        WorkoutConfirmationOrObjectionResponse response1 = responses.get(1);
        assertThat(response1.getNickname()).isEqualTo(creator.getNickname());
        assertThat(response1.getProfileImageUrl()).isEqualTo(creator.getProfileImageName());
        assertThat(response1.getWorkoutConfirmationId()).isEqualTo(objection.getWorkoutConfirmation().getId());
        assertThat(response1.getObjectionId()).isEqualTo(objection.getId());
        assertThat(response1.getIsMine()).isEqualTo(false);
        assertThat(response1.getIsObjection()).isEqualTo(true);
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

        @Nested
        class 이의_신청_목록 {
            @Test
            void 투표하지_않은_이의_신청_목록을_최신순으로_확인_한다() {
                // given
                User creator = persister.persistUser();
                User user = persister.persistUser();
                Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS, 100, 5);
                Worker creatorWorker = persister.persistWorker(creator, workspace);
                Worker userWorker = persister.persistWorker(user, workspace);
                Mission mission = persister.persistMission(workspace, 1);
                Mission mission1 = persister.persistMission(workspace, 5);

                List<Objection> objections = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
                    persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 2, mission1, 2), workoutConfirmation);
                    objections.add(persister.persistObjection(userWorker, true, workoutConfirmation));
                }

                persister.persistVote(creatorWorker, objections.get(0), true);
                persister.persistVote(creatorWorker, objections.get(1), true);
                persister.persistVote(creatorWorker, objections.get(3), true);

                // when
                List<ObjectionAlarmResponse> responses = workspaceQueryService.getObjections(creator, workspace.getId(), 0, ObjectionStatus.INCOMPLETION);

                // then
                assertThat(responses).hasSize(7);
                assertThat(responses.get(6).getObjectionId()).isEqualTo(objections.get(2).getId());
                assertThat(responses.get(6).getVoteCompletion()).isEqualTo(false);
                assertThat(responses.get(6).getTargetWorkerNickname()).isEqualTo(creator.getNickname());
                assertThat(responses.get(5).getObjectionId()).isEqualTo(objections.get(4).getId());
            }

            @Test
            void 진행중인_이의_신청_목록을_최신순으로_확인_한다() {
                // given
                User creator = persister.persistUser();
                User user = persister.persistUser();
                Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS, 100, 5);
                Worker creatorWorker = persister.persistWorker(creator, workspace);
                Worker userWorker = persister.persistWorker(user, workspace);
                Mission mission = persister.persistMission(workspace, 1);
                Mission mission1 = persister.persistMission(workspace, 5);

                List<Objection> objections = new ArrayList<>();
                List<Boolean> isInProgressStatus = List.of(true, true, false, false, true);
                for (int i = 0; i < 5; i++) {
                    WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
                    persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 2, mission1, 2), workoutConfirmation);
                    objections.add(persister.persistObjection(userWorker, isInProgressStatus.get(i), workoutConfirmation));
                }

                // when
                List<ObjectionAlarmResponse> responses = workspaceQueryService.getObjections(creator, workspace.getId(), 0, ObjectionStatus.IN_PROGRESS);

                // then
                assertThat(responses).hasSize(3);
                assertThat(responses.get(0).getObjectionId()).isEqualTo(objections.get(4).getId());
                assertThat(responses.get(0).getVoteCompletion()).isEqualTo(false);
                assertThat(responses.get(0).getTargetWorkerNickname()).isEqualTo(creator.getNickname());
                assertThat(responses.get(1).getObjectionId()).isEqualTo(objections.get(1).getId());
                assertThat(responses.get(2).getObjectionId()).isEqualTo(objections.get(0).getId());
            }

            @Test
            void 종료된_이의_신청_목록을_최신순으로_확인_한다() {
                // given
                User creator = persister.persistUser();
                User user = persister.persistUser();
                Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS, 100, 5);
                Worker creatorWorker = persister.persistWorker(creator, workspace);
                Worker userWorker = persister.persistWorker(user, workspace);
                Mission mission = persister.persistMission(workspace, 1);
                Mission mission1 = persister.persistMission(workspace, 5);

                List<Objection> objections = new ArrayList<>();
                List<Boolean> isInProgressStatus = List.of(false, true, false, false, true);
                for (int i = 0; i < 5; i++) {
                    WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
                    persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 2, mission1, 2), workoutConfirmation);
                    objections.add(persister.persistObjection(userWorker, isInProgressStatus.get(i), workoutConfirmation));
                }

                // when
                List<ObjectionAlarmResponse> responses = workspaceQueryService.getObjections(creator, workspace.getId(), 0, ObjectionStatus.CLOSED);

                // then
                assertThat(responses).hasSize(3);
                assertThat(responses.get(0).getObjectionId()).isEqualTo(objections.get(3).getId());
                assertThat(responses.get(1).getObjectionId()).isEqualTo(objections.get(2).getId());
                assertThat(responses.get(2).getObjectionId()).isEqualTo(objections.get(0).getId());
            }

        }

    }
}
