package gymmi.repository;

import gymmi.Fixtures;
import gymmi.entity.Task;
import gymmi.entity.User;
import gymmi.entity.Worker;
import gymmi.entity.Workspace;
import gymmi.global.QuerydslConfig;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static gymmi.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@Import(QuerydslConfig.class)
@DataJpaTest
class WorkerRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    WorkerRepository workerRepository;

    @Test
    void 워커를_찾는다() {
        // given
        User defaultUser = Fixtures.USER__DEFAULT_USER;

        Workspace workspace = Workspace.builder()
                .creator(defaultUser)
                .name(WORKSPACE__SATISFIED_NAME)
                .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
                .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
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
        assertThat(result).isNotEmpty();
    }

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
