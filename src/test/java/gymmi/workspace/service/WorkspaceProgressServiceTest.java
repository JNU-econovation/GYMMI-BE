package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.photoboard.repository.PhotoFeedRepository;
import gymmi.service.S3Service;
import gymmi.workspace.mission.controller.request.MissionRequest;
import gymmi.workspace.mission.repository.FavoriteMissionRepository;
import gymmi.workspace.mission.repository.MissionRepository;
import gymmi.workspace.objection.controller.request.ObjectionRequest;
import gymmi.workspace.objection.repository.ObjectionRepository;
import gymmi.workspace.vote.controller.request.VoteRequest;
import gymmi.workspace.vote.repository.VoteRepository;
import gymmi.workspace.workout.controller.request.WorkingMissionInWorkspaceRequest;
import gymmi.workspace.workout.controller.request.WorkoutRequest;
import gymmi.workspace.workspace.controller.request.CreatingWorkspaceRequest;
import gymmi.workspace.workspace.domain.WorkspaceStatus;
import gymmi.workspace.mission.domain.entity.Mission;
import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workout.domain.entity.WorkoutConfirmation;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.workspace.domain.WorkspaceCreationValidator;
import gymmi.workspace.workspace.repository.WorkerRepository;
import gymmi.workspace.workout.repository.WorkoutHistoryRepository;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import gymmi.workspace.workspace.service.WorkspacePreparingService;
import gymmi.workspace.workspace.service.WorkspaceProgressService;
import jakarta.persistence.EntityManager;
import org.instancio.Instancio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static gymmi.exceptionhandler.message.ErrorCode.EXCEED_MAX_JOINED_WORKSPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

class WorkspaceProgressServiceTest extends IntegrationTest {

    @Autowired
    WorkspaceProgressService workspaceProgressService;

    @Autowired
    WorkspacePreparingService workspacePreparingService;


    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    MissionRepository missionRepository;
    @Autowired
    WorkoutHistoryRepository workoutHistoryRepository;
    @Autowired
    FavoriteMissionRepository favoriteMissionRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    ObjectionRepository objectionRepository;
    @Autowired
    PhotoFeedRepository photoFeedRepository;

    @MockBean
    S3Service s3Service;

    @Autowired
    EntityManager entityManager;

    @Nested
    class 워크스페이스_생성 {

        @Test
        void 참여하면서_완료_되지_않은_워크스페이스가_5개_이상_인_경우_예외가_발생한다() {
            // given
            User user = persister.persistUser();
            persistWorkspacesNotCompletedWithWorker(user, 5);

            CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
                    .goalScore(WorkspaceCreationValidator.MIN_GOAL_SCORE)
                    .headCount(WorkspaceCreationValidator.MIN_HEAD_COUNT)
                    .name("지미")
                    .task(Instancio.gen().string().get())
                    .missionBoard(
                            List.of(new MissionRequest(
                                    Instancio.gen().string().maxLength(Mission.MAX_NAME_LENGTH).get(),
                                    Mission.MIN_SCORE)))
                    .build();

            // when, then
            assertThatThrownBy(() -> workspacePreparingService.setUpWorkspace(user, request))
                    .hasMessage(EXCEED_MAX_JOINED_WORKSPACE.getMessage());
        }

        @Test
        void 워크스페이스_이름이_이미_존재하는_경우_예외가_발생한다() {
            // given
            User user = persister.persistUser();
            Workspace workspace = persister.persistWorkspace(user);
            String workspaceName = workspace.getName();

            CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
                    .goalScore(WorkspaceCreationValidator.MIN_GOAL_SCORE)
                    .headCount(WorkspaceCreationValidator.MIN_HEAD_COUNT)
                    .name(workspaceName)
                    .task(Instancio.gen().string().get())
                    .missionBoard(
                            List.of(new MissionRequest(
                                    Instancio.gen().string().maxLength(Mission.MAX_NAME_LENGTH).get(),
                                    Mission.MIN_SCORE)))
                    .build();

            // when, then
            assertThatThrownBy(() -> workspacePreparingService.setUpWorkspace(user, request))
                    .hasMessage(ErrorCode.ALREADY_USED_WORKSPACE_NAME.getMessage());
        }

    }


    @Test
    void 방장이_워크스페이스를_떠나는_경우_워크스페이스와_관련_정보도_삭제된다() {
        // given
        User user = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(user, WorkspaceStatus.PREPARING);
        Worker worker = persister.persistWorker(user, workspace);
        List<Mission> missions = persister.persistMissions(workspace, 3);
        persister.persistFavoriteMission(worker, missions.get(0));

        // when
        workspacePreparingService.leaveWorkspace(user, workspace.getId());

        // then
        assertThat(workspaceRepository.findById(workspace.getId())).isEmpty();
        assertThat(workerRepository.findById(worker.getId())).isEmpty();
        assertThat(missionRepository.getAllByWorkspaceId(workspace.getId())).isEmpty();
        assertThat(favoriteMissionRepository.findAll()).isEmpty();
        assertThat(workerRepository.findById(worker.getId())).isEmpty();
    }


    @Nested
    class 워크스페이스_운동 {
        @Test
        void 워크스페이스_운동시_참여자의_점수가_반영되고_연동_여부에_따라_사진_피드가_등록_된다() {
            // given
            User user = persister.persistUser();
            Workspace workspace = persister.persistWorkspace(user, WorkspaceStatus.IN_PROGRESS);
            Worker worker = persister.persistWorker(user, workspace);
            Mission mission = persister.persistMission(workspace, 10);
            int count = 100;

            List<WorkingMissionInWorkspaceRequest> requests = List.of(
                    new WorkingMissionInWorkspaceRequest(mission.getId(), count)
            );
            WorkoutRequest request = Instancio.of(WorkoutRequest.class)
                    .set(field(WorkoutRequest::getMissions), requests)
                    .set(field(WorkoutRequest::getWillLink), true)
                    .create();
            assertThat(worker.getContributedScore()).isEqualTo(0);
            given(s3Service.copy(any(), any(), any())).willReturn(UUID.randomUUID().toString());

            // when
            workspaceProgressService.workMissionsInWorkspace(user, workspace.getId(), request);

            // then
            assertThat(workoutHistoryRepository.getAllByWorkerId(worker.getId())).hasSize(1);
            assertThat(worker.getContributedScore()).isEqualTo(mission.getScore() * count);
            assertThat(workspace.getStatus()).isEqualTo(WorkspaceStatus.COMPLETED);
            assertThat(assertThat(photoFeedRepository.findAll()).hasSize(1));
        }

        @Test
        void 워크스페이스_일일_운동_횟수를_초과한_경우_예외가_발생_한다() {
            // given
            User user = persister.persistUser();
            Workspace workspace = persister.persistWorkspace(user, WorkspaceStatus.IN_PROGRESS);
            Worker worker = persister.persistWorker(user, workspace);
            Mission mission = persister.persistMission(workspace, 10);

            persister.persistWorkoutHistoryAndApply(worker, Map.of(mission, 2));
            persister.persistWorkoutHistoryAndApply(worker, Map.of(mission, 2));
            persister.persistWorkoutHistoryAndApply(worker, Map.of(mission, 2));

            List<WorkingMissionInWorkspaceRequest> requests = List.of(
                    new WorkingMissionInWorkspaceRequest(mission.getId(), 10)
            );
            WorkoutRequest request = Instancio.of(WorkoutRequest.class)
                    .set(field(WorkoutRequest::getMissions), requests)
                    .set(field(WorkoutRequest::getWillLink), true)
                    .create();

            // when, then
            assertThatThrownBy(() -> workspaceProgressService.workMissionsInWorkspace(user, workspace.getId(), request))
                    .hasMessage(ErrorCode.EXCEED_MAX_DAILY_WORKOUT_HISTORY_COUNT.getMessage());
        }

    }


//    @Test
//    void 미션을_즐겨찾기에_추가_또는_삭제_한다() {
//        // given
//        User user = persister.persistUser();
//        Workspace workspace = persister.persistWorkspace(user);
//        Worker worker = persister.persistWorker(user, workspace);
//        Mission mission = persister.persistMission(workspace, 10);
//
//        // when, then
//        workspaceProgressService.toggleRegistrationOfFavoriteMission(user, workspace.getId(), mission.getId());
//        assertThat(favoriteMissionRepository.findByWorkerIdAndMissionId(worker.getId(), mission.getId())).isNotEmpty();
//
//        workspaceProgressService.toggleRegistrationOfFavoriteMission(user, workspace.getId(), mission.getId());
//        assertThat(favoriteMissionRepository.findByWorkerIdAndMissionId(worker.getId(), mission.getId())).isEmpty();
//    }

    @Test
    void 운동인증에_이의_신청을_한다() {
        // given
        User creator = persister.persistUser();
        User user = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS);
        Worker creatorWorker = persister.persistWorker(creator, workspace);
        Worker userWorker = persister.persistWorker(user, workspace);
        Mission mission = persister.persistMission(workspace, 1);
        Mission mission1 = persister.persistMission(workspace, 5);
        WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
        WorkoutHistory workoutHistory = persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 2, mission1, 2), workoutConfirmation);
        ObjectionRequest request = new ObjectionRequest("이유");
        Long workoutConfirmationId = workoutHistory.getWorkoutConfirmation().getId();

        // when
        workspaceProgressService.objectToWorkoutHistory(user, workspace.getId(), workoutConfirmationId, request.getReason());

        // then
        assertThat(objectionRepository.findByWorkoutHistoryId(workoutConfirmationId)).isNotEmpty();
    }

    @Test
    void 이의신청_투표를_통해_이의신청이_찬성되어_점수가_몰수된다() {
        // given
        User creator = persister.persistUser();
        User user = persister.persistUser();
        User user1 = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS, 100, 3);
        Worker creatorWorker = persister.persistWorker(creator, workspace);
        Worker userWorker = persister.persistWorker(user, workspace);
        Worker user1Worker = persister.persistWorker(user1, workspace);
        WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
        Mission mission = persister.persistMission(workspace, 10);
        persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 1), workoutConfirmation);
        Objection objection = persister.persistObjection(userWorker, true, workoutConfirmation);
        persister.persistVote(userWorker, objection, true);
        persister.persistVote(creatorWorker, objection, false);
        VoteRequest request = new VoteRequest(true);

        // when
        workspaceProgressService.voteToObjection(user1, workspace.getId(), objection.getId(), request.getWillApprove());

        // then
        WorkoutHistory workoutHistory = workoutHistoryRepository.findByWorkoutConfirmationIdOrThrow(workoutConfirmation.getId());
        assertThat(voteRepository.findAll().size()).isEqualTo(3);
        assertThat(objection.isInProgress()).isEqualTo(false);
        assertThat(workoutHistory.isRejected()).isFalse();
        assertThat(userWorker.getContributedScore()).isEqualTo(0);
    }

//    @Test
//    void 투표가_안되었지만_시간이_지난_이의신청은_찬성표를_통해_자동으로_종료시킨다() {
//        // given
//        User creator = persister.persistUser();
//        User user = persister.persistUser();
//        User user1 = persister.persistUser();
//        User user2 = persister.persistUser();
//        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.IN_PROGRESS, 100, 4);
//        Worker creatorWorker = persister.persistWorker(creator, workspace);
//        Worker userWorker = persister.persistWorker(user, workspace);
//        Worker user1Worker = persister.persistWorker(user1, workspace);
//        Worker user2Worker = persister.persistWorker(user2, workspace);
//        WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
//        Mission mission = persister.persistMission(workspace, 10);
//        persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 1), workoutConfirmation);
//
//        Objection objection = persister.persistObjection(userWorker, true, workoutConfirmation);
//        persister.persistVote(userWorker, objection, false);
//        ReflectionTestUtils.setField(objection, "createdAt", LocalDateTime.now().minusHours(25));
//
//        // when
//        workspaceProgressService.terminateExpiredObjection(creator, workspace.getId());
//
//        // then
//        entityManager.flush();
//        entityManager.clear();
//        Objection refreshObjection = objectionRepository.findByIdOrThrow(objection.getId());
//        assertThat(refreshObjection.isInProgress()).isFalse();
//        assertThat(refreshObjection.getVoteCount()).isEqualTo(4);
//        assertThat(refreshObjection.getApprovalCount()).isEqualTo(3);
//
//    }

    @Test
    void 최종_결과_확인시_진행중인_이의_신청이_존재하는_경우_예외가_발생한다() {
        // given
        User creator = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.COMPLETED, 100, 4);
        Worker creatorWorker = persister.persistWorker(creator, workspace);
        Mission mission = persister.persistMission(workspace, 10);

        WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
        persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 1), workoutConfirmation);

        Objection objection = persister.persistObjection(creatorWorker, true, workoutConfirmation);

        // when, then
        assertThatThrownBy(() -> workspaceProgressService.getWorkspaceResult(creator, workspace.getId()))
                .hasMessage(ErrorCode.EXIST_OBJECTION_IN_PROGRESS.getMessage());
    }

    private List<Workspace> persistWorkspacesNotCompletedWithWorker(User user, int size) {
        List<Workspace> workspaces = Instancio.ofList(Workspace.class)
                .size(size)
                .generate(field(Workspace::getStatus), gen -> gen.enumOf(WorkspaceStatus.class)
                        .excluding(WorkspaceStatus.COMPLETED, WorkspaceStatus.FULLY_COMPLETED))
                .set(field(Workspace::getGoalScore), WorkspaceCreationValidator.MIN_GOAL_SCORE)
                .set(field(Workspace::getHeadCount), WorkspaceCreationValidator.MIN_HEAD_COUNT)
                .set(field(Workspace::getCreator), user)
                .ignore(field(Workspace::getId))
                .create();
        workspaceRepository.saveAll(workspaces);
        for (Workspace workspace : workspaces) {
            Worker worker = new Worker(user, workspace);
            entityManager.persist(worker);
        }
        return workspaces;
    }
}
