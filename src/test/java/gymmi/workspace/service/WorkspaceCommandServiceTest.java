package gymmi.workspace.service;

import static gymmi.exceptionhandler.message.ErrorCode.EXCEED_MAX_JOINED_WORKSPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;

import gymmi.entity.User;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Mission;
import gymmi.workspace.domain.entity.Task;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.repository.FavoriteMissionRepository;
import gymmi.workspace.repository.MissionRepository;
import gymmi.workspace.repository.WorkoutHistoryRepository;
import gymmi.workspace.repository.WorkerRepository;
import gymmi.workspace.repository.WorkspaceRepository;
import gymmi.workspace.request.CreatingWorkspaceRequest;
import gymmi.workspace.request.MissionRequest;
import gymmi.workspace.request.WorkingMissionInWorkspaceRequest;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WorkspaceCommandServiceTest extends IntegrationTest {

    @Autowired
    WorkspaceCommandService workspaceCommandService;

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
    EntityManager entityManager;

    @Nested
    class 워크스페이스_생성 {

        @Test
        void 참여하면서_완료_되지_않은_워크스페이스가_5개_이상_인_경우_예외가_발생한다() {
            // given
            User user = persistUser();
            persistWorkspacesNotCompletedWithWorker(user, 5);

            CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
                    .goalScore(Workspace.MIN_GOAL_SCORE)
                    .headCount(Workspace.MIN_HEAD_COUNT)
                    .name("지미")
                    .task(Instancio.gen().string().get())
                    .missionBoard(
                            List.of(new MissionRequest(
                                    Instancio.gen().string().maxLength(Mission.MAX_NAME_LENGTH).get(),
                                    Mission.MIN_SCORE)))
                    .build();

            // when, then
            assertThatThrownBy(() -> workspaceCommandService.createWorkspace(user, request))
                    .hasMessage(EXCEED_MAX_JOINED_WORKSPACE.getMessage());
        }

        @Test
        void 워크스페이스_이름이_이미_존재하는_경우_예외가_발생한다() {
            // given
            User user = persistUser();
            Workspace workspace = persistWorkspace(user);
            String workspaceName = workspace.getName();

            CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
                    .goalScore(Workspace.MIN_GOAL_SCORE)
                    .headCount(Workspace.MIN_HEAD_COUNT)
                    .name(workspaceName)
                    .task(Instancio.gen().string().get())
                    .missionBoard(
                            List.of(new MissionRequest(
                                    Instancio.gen().string().maxLength(Mission.MAX_NAME_LENGTH).get(),
                                    Mission.MIN_SCORE)))
                    .build();

            // when, then
            assertThatThrownBy(() -> workspaceCommandService.createWorkspace(user, request))
                    .hasMessage(ErrorCode.ALREADY_USED_WORKSPACE_NAME.getMessage());
        }

    }


    @Test
    void 방장이_워크스페이스를_떠나는_경우_워크스페이스도_삭제된다() {
        // given
        User user = persistUser();
        Workspace workspace = persistWorkspace(user, WorkspaceStatus.PREPARING);
        Worker worker = persistWorker(user, workspace);

        // when
        workspaceCommandService.leaveWorkspace(user, workspace.getId());

        // then
        assertThat(workspaceRepository.findById(workspace.getId())).isEmpty();
        assertThat(workerRepository.findById(worker.getId())).isEmpty();
        assertThat(entityManager.find(Task.class, worker.getTask().getId())).isNull();
    }


    @Test
    void 워크스페이스_운동시_참여자의_점수가_반영된다() {
        // given
        User user = persistUser();
        Workspace workspace = persistWorkspace(user, WorkspaceStatus.IN_PROGRESS);
        Worker worker = persistWorker(user, workspace);
        List<Mission> missions = persistMissions(workspace, 1);
        Mission mission = missions.get(0);
        int count = 100;

        List<WorkingMissionInWorkspaceRequest> requests = List.of(
                new WorkingMissionInWorkspaceRequest(mission.getId(), count)
        );
        assertThat(worker.getContributedScore()).isEqualTo(0);

        // when
        workspaceCommandService.workMissionsInWorkspace(user, workspace.getId(), requests);

        // then
        assertThat(workoutHistoryRepository.getAllByWorkerId(worker.getId())).hasSize(1);
        assertThat(worker.getContributedScore()).isEqualTo(mission.getScore() * count);
        assertThat(workspace.getStatus()).isEqualTo(WorkspaceStatus.COMPLETED);
    }

    @Test
    void 완료된_워크스페이스인_경우_테스크_뽑기를_한다() {
        // given

        // when

        // then

    }

    @Test
    void 미션을_즐겨찾기에_추가_또는_삭제_한다() {
        // given
        User user = persistUser();
        Workspace workspace = persistWorkspace(user);
        Worker worker = persister.persistWorker(user, workspace);
        Mission mission = persister.persistMission(workspace, 10);

        // when, then
        workspaceCommandService.toggleRegistrationOfFavoriteMission(user, workspace.getId(), mission.getId());
        assertThat(favoriteMissionRepository.findByWorkerIdAndMissionId(worker.getId(), mission.getId())).isNotEmpty();

        workspaceCommandService.toggleRegistrationOfFavoriteMission(user, workspace.getId(), mission.getId());
        assertThat(favoriteMissionRepository.findByWorkerIdAndMissionId(worker.getId(), mission.getId())).isEmpty();
    }

    private Worker persistWorker(User user, Workspace workspace) {
        Worker worker = new Worker(user, workspace, new Task(Instancio.gen().string().get()));
        workerRepository.save(worker);
        return worker;
    }

    private User persistUser() {
        User user = Instancio.of(User.class)
                .set(field("isResigned"), false)
                .ignore(field(User::getId))
                .create();
        entityManager.persist(user);
        return user;
    }

    private List<Workspace> persistWorkspacesNotCompletedWithWorker(User user, int size) {
        List<Workspace> workspaces = Instancio.ofList(Workspace.class)
                .size(size)
                .generate(field(Workspace::getStatus), gen -> gen.enumOf(WorkspaceStatus.class)
                        .excluding(WorkspaceStatus.COMPLETED, WorkspaceStatus.FULLY_COMPLETED))
                .set(field(Workspace::getGoalScore), Workspace.MIN_GOAL_SCORE)
                .set(field(Workspace::getHeadCount), Workspace.MIN_HEAD_COUNT)
                .set(field(Workspace::getCreator), user)
                .ignore(field(Workspace::getId))
                .create();
        workspaceRepository.saveAll(workspaces);
        for (Workspace workspace : workspaces) {
            Worker worker = new Worker(user, workspace, new Task(Instancio.gen().string().get()));
            entityManager.persist(worker);
        }
        return workspaces;
    }

    private Workspace persistWorkspace(User creator) {
        Workspace workspace = Instancio.of(Workspace.class)
                .generate(field(Workspace::getStatus), gen -> gen.enumOf(WorkspaceStatus.class))
                .set(field(Workspace::getGoalScore), Workspace.MIN_GOAL_SCORE)
                .set(field(Workspace::getHeadCount), Workspace.MIN_HEAD_COUNT)
                .set(field(Workspace::getCreator), creator)
                .ignore(field(Workspace::getId))
                .create();
        workspaceRepository.save(workspace);
        return workspace;
    }

    private Workspace persistWorkspace(User creator, WorkspaceStatus workspaceStatus) {
        Workspace workspace = Instancio.of(Workspace.class)
                .set(field(Workspace::getStatus), workspaceStatus)
                .set(field(Workspace::getGoalScore), Workspace.MIN_GOAL_SCORE)
                .set(field(Workspace::getHeadCount), Workspace.MIN_HEAD_COUNT)
                .set(field(Workspace::getCreator), creator)
                .ignore(field(Workspace::getId))
                .create();
        workspaceRepository.save(workspace);
        return workspace;
    }

    private List<Mission> persistMissions(Workspace workspace, int size) {
        List<Mission> missions = Instancio.ofList(Mission.class)
                .size(size)
                .set(Select.field(Mission::getWorkspace), workspace)
                .generate(Select.field(Mission::getScore),
                        gen -> gen.ints().range(Mission.MIN_SCORE, Mission.MAX_SCORE))
                .ignore(Select.field(Mission::getId))
                .create();
        missionRepository.saveAll(missions);
        return missions;
    }

}
