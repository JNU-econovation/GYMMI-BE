package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.service.domain.workspace.WorkspaceStatus;
import gymmi.workspace.service.domain.mission.Mission;
import gymmi.workspace.service.domain.workspace.Worker;
import gymmi.workspace.service.domain.workspace.Workspace;
import gymmi.workspace.service.domain.workspace.WorkspaceCreationValidator;
import gymmi.workspace.repository.*;
import gymmi.workspace.request.*;
import jakarta.persistence.EntityManager;
import org.instancio.Instancio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static gymmi.exceptionhandler.message.ErrorCode.EXCEED_MAX_JOINED_WORKSPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.BDDMockito.any;

class WorkspacePreparingTest extends IntegrationTest {

    @Autowired
    WorkspacePreparingService workspacePreparingService;

    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    MissionRepository missionRepository;
    @Autowired
    FavoriteMissionRepository favoriteMissionRepository;

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
