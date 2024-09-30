package gymmi.repository;

import static gymmi.Fixtures.WORKSPACE__SATISFIED_GOAL_SCORE;
import static gymmi.Fixtures.WORKSPACE__SATISFIED_HEAD_COUNT;
import static gymmi.Fixtures.WORKSPACE__SATISFIED_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import gymmi.Fixtures;
import gymmi.entity.User;
import gymmi.global.QuerydslConfig;
import gymmi.workspace.domain.Task;
import gymmi.workspace.domain.Worker;
import gymmi.workspace.domain.Workspace;
import gymmi.workspace.repository.WorkerRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(QuerydslConfig.class)
@DataJpaTest
class WorkerRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    WorkerRepository workerRepository;

    @Test
    void 워크스페이스_참가자를_저장할때_작성한_테스크도_같이_저장된다() {
        // given
        User user = Fixtures.USER__DEFAULT_USER;
        Workspace workspace = Workspace.builder()
                .creator(user)
                .name(WORKSPACE__SATISFIED_NAME)
                .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
                .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
                .description(null)
                .tag(null)
                .build();
        entityManager.persist(user);
        entityManager.persist(workspace);

        Task task = new Task("안녕하세요");
        Worker worker = new Worker(user, workspace, task);

        // when
        entityManager.persist(worker);

        // then
        assertThat(worker.getTask().getId()).isNotNull();
    }

}
