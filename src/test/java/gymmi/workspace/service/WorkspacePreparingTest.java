package gymmi.workspace.service;

import gymmi.user.domain.User;
import gymmi.workspace.mission.repository.FavoriteMissionRepository;
import gymmi.workspace.mission.repository.MissionRepository;
import gymmi.workspace.workspace.domain.WorkspaceStatus;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.workspace.domain.WorkspaceCreationValidator;
import gymmi.workspace.workspace.repository.WorkerRepository;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import gymmi.workspace.workspace.service.WorkspacePreparingService;
import jakarta.persistence.EntityManager;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
