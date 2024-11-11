package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.repository.*;
import gymmi.workspace.request.*;
import jakarta.persistence.EntityManager;
import org.instancio.Instancio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static gymmi.exceptionhandler.message.ErrorCode.EXCEED_MAX_JOINED_WORKSPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;

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
    TackleRepository tackleRepository;

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
            User user = persister.persistUser();
            Workspace workspace = persister.persistWorkspace(user);
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
        User user = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(user, WorkspaceStatus.PREPARING);
        Worker worker = persister.persistWorker(user, workspace);

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
        User user = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(user, WorkspaceStatus.IN_PROGRESS);
        Worker worker = persister.persistWorker(user, workspace);
        List<Mission> missions = persister.persistMissions(workspace, 1);
        Mission mission = missions.get(0);
        int count = 100;

        List<WorkingMissionInWorkspaceRequest> requests = List.of(
                new WorkingMissionInWorkspaceRequest(mission.getId(), count)
        );
        WorkoutRequest request = Instancio.of(WorkoutRequest.class)
                .set(field(WorkoutRequest::getMissions), requests)
                .create();
        assertThat(worker.getContributedScore()).isEqualTo(0);

        // when
        workspaceCommandService.workMissionsInWorkspace(user, workspace.getId(), request);

        // then
        assertThat(workoutHistoryRepository.getAllByWorkerId(worker.getId())).hasSize(1);
        assertThat(worker.getContributedScore()).isEqualTo(mission.getScore() * count);
        assertThat(workspace.getStatus()).isEqualTo(WorkspaceStatus.COMPLETED);
    }

    @Test
    void 미션을_즐겨찾기에_추가_또는_삭제_한다() {
        // given
        User user = persister.persistUser();
        Workspace workspace = persister.persistWorkspace(user);
        Worker worker = persister.persistWorker(user, workspace);
        Mission mission = persister.persistMission(workspace, 10);

        // when, then
        workspaceCommandService.toggleRegistrationOfFavoriteMission(user, workspace.getId(), mission.getId());
        assertThat(favoriteMissionRepository.findByWorkerIdAndMissionId(worker.getId(), mission.getId())).isNotEmpty();

        workspaceCommandService.toggleRegistrationOfFavoriteMission(user, workspace.getId(), mission.getId());
        assertThat(favoriteMissionRepository.findByWorkerIdAndMissionId(worker.getId(), mission.getId())).isEmpty();
    }

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
        WorkoutProof workoutProof = new WorkoutProof("creator", "a");
        WorkoutHistory workoutHistory = persister.persistWorkoutHistoryAndApply(creatorWorker, Map.of(mission, 2, mission1, 2), workoutProof);
        TackleRequest request = new TackleRequest("이유");
        Long workoutConfirmationId = workoutHistory.getWorkoutProof().getId();

        // when
        workspaceCommandService.tackleToWorkoutConfirmation(user, workspace.getId(), workoutConfirmationId, request);

        // then
        assertThat(tackleRepository.findByWorkoutConfirmationId(workoutConfirmationId)).isNotEmpty();
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

}
