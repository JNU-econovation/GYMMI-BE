package gymmi.repository;

import gymmi.Fixtures;
import gymmi.entity.User;
import gymmi.entity.Worker;
import gymmi.entity.Workspace;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
class WorkerRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    WorkerRepository workerRepository;

    @Test
    void 워커를_찾는다() {
        // given
        User defaultUser = Fixtures.USER_DEFAULT;

        Workspace workspace = Workspace.builder()
                .creator(defaultUser)
                .name(Fixtures.Workspace.SATISFIED_NAME)
                .headCount(Fixtures.Workspace.SATISFIED_HEAD_COUNT)
                .goalScore(Fixtures.Workspace.SATISFIED_GOAL_SCORE)
                .description(null)
                .tag(null)
                .build();
        entityManager.persist(defaultUser);
        entityManager.persist(workspace);

        Worker worker = Worker.builder()
                .user(defaultUser)
                .workspace(workspace)
                .build();
        entityManager.persist(worker);

        // when
        Optional<Worker> result = workerRepository.findByUserIdAndWorkspaceId(defaultUser.getId(), workspace.getId());

        // then
        Assertions.assertThat(result).isNotEmpty();
    }

}
