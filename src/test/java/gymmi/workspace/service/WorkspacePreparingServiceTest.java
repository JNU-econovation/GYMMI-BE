package gymmi.workspace.service;

import gymmi.etc.domain.entity.User;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.photoboard.repository.PhotoFeedRepository;
import gymmi.etc.service.S3Service;
import gymmi.workspace.mission.repository.FavoriteMissionRepository;
import gymmi.workspace.mission.repository.MissionRepository;
import gymmi.workspace.objection.controller.request.ObjectionRequest;
import gymmi.workspace.objection.repository.ObjectionRepository;
import gymmi.workspace.objection.service.ObjectionService;
import gymmi.workspace.vote.controller.request.VoteRequest;
import gymmi.workspace.vote.repository.VoteRepository;
import gymmi.workspace.vote.service.VoteService;
import gymmi.workspace.workout.controller.request.WorkingMissionInWorkspaceRequest;
import gymmi.workspace.workout.controller.request.WorkoutRequest;
import gymmi.workspace.workout.service.WorkoutService;
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
import jakarta.persistence.EntityManager;
import org.instancio.Instancio;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

class WorkspacePreparingServiceTest extends IntegrationTest {

    @Autowired
    WorkspacePreparingService workspacePreparingService;

    @Autowired
    WorkoutService workoutService;

    @Autowired
    ObjectionService objectionService;

    @Autowired
    VoteService voteService;

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


    @Disabled
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

//    @Test
//    void 최종_결과_확인시_진행중인_이의_신청이_존재하는_경우_예외가_발생한다() {
//        // given
//        User creator = persister.persistUser();
//        Workspace workspace = persister.persistWorkspace(creator, WorkspaceStatus.COMPLETED, 100, 4);
//        Worker creatorWorker = persister.persistWorker(creator, workspace);
//        Mission mission = persister.persistMission(workspace, 10);
//
//        WorkoutConfirmation workoutConfirmation = persister.persistWorkoutConfirmation();
//        persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 1), workoutConfirmation);
//
//        Objection objection = persister.persistObjection(creatorWorker, true, workoutConfirmation);
//
//        // when, then
//        assertThatThrownBy(() -> workspaceProgressService.getWorkspaceResult(creator, workspace.getId()))
//                .hasMessage(ErrorCode.EXIST_OBJECTION_IN_PROGRESS.getMessage());
//    }

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
