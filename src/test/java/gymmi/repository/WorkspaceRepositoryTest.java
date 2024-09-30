package gymmi.repository;

import static org.instancio.Select.field;

import gymmi.entity.User;
import gymmi.workspace.domain.Workspace;
import gymmi.workspace.repository.WorkspaceRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WorkspaceRepositoryTest extends RepositoryTest {

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    EntityManager entityManager;

    @Disabled
    @Test
    void 여러_워크스페이스의_달성_점수를_조회한다() {
        // given
        User user = persistUser();
        Workspace workspace = Instancio.of(Workspace.class)
                .ignore(field(Workspace::getId))
                .set(field(Workspace::getCreator), user)
                .create();

        Workspace workspace1 = Instancio.of(Workspace.class)
                .ignore(field(Workspace::getId))
                .set(field(Workspace::getCreator), user)
                .create();
        entityManager.persist(workspace);
        entityManager.persist(workspace1);

        workspaceRepository.getAchievementScoresIn(List.of(workspace, workspace1));
    }

    private User persistUser() {
        User user = Instancio.of(User.class)
                .set(field("isResigned"), false)
                .ignore(field(User::getId))
                .create();
        entityManager.persist(user);
        return user;
    }
}
