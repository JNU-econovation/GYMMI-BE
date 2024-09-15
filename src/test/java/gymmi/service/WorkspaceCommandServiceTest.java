package gymmi.service;

import static gymmi.Fixtures.MISSION__SATISFIED_MISSION_NAME;
import static gymmi.Fixtures.MISSION__SATISFIED_MISSION_SCORE;
import static gymmi.Fixtures.TASK__DEFAULT_TASK;
import static gymmi.Fixtures.WORKSPACE__SATISFIED_GOAL_SCORE;
import static gymmi.Fixtures.WORKSPACE__SATISFIED_HEAD_COUNT;
import static gymmi.Fixtures.WORKSPACE__SATISFIED_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import gymmi.Fixtures;
import gymmi.entity.User;
import gymmi.repository.UserRepository;
import gymmi.workspace.domain.Task;
import gymmi.workspace.repository.MissionRepository;
import gymmi.workspace.repository.TaskRepository;
import gymmi.workspace.repository.WorkerRepository;
import gymmi.workspace.repository.WorkspaceRepository;
import gymmi.workspace.request.CreatingWorkspaceRequest;
import gymmi.workspace.request.MissionRequest;
import gymmi.workspace.service.WorkspaceCommandService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class WorkspaceCommandServiceTest {

    @Autowired
    WorkspaceCommandService workspaceCommandService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    MissionRepository missionRepository;
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void 워크스페이스_생성() {
        // given
        User user = Fixtures.USER__DEFAULT_USER;
        entityManager.persist(user);
        CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
                .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
                .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
                .name(WORKSPACE__SATISFIED_NAME)
                .task(TASK__DEFAULT_TASK)
                .missionBoard(
                        List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME, MISSION__SATISFIED_MISSION_SCORE)))
                .build();

        // when
        Long result = workspaceCommandService.createWorkspace(user, request);

        // then
        assertThat(result).isNotNull();
    }

    @Nested
    class 워크스페이스_나가기 {
        @Test
        void 방장이_방을_나가면_미션과_워크스페이스는_삭제된다() {
            // given

            // when
            workspaceCommandService.leaveWorkspace(null, null);

            // then

        }

        @Test
        void 워크스페이스를_나가면_등록한_테스크는_삭제된다() {
            // given

            // when

            // then

        }

        @Test
        void 스프링_배치_테스트() {
            // given
            for (int i = 0; i < 10; i++) {
                Task task = new Task("" + i);
                taskRepository.save(task);
            }
            entityManager.flush();
            entityManager.clear();

            System.out.println("============================");
            List<Task> tasks = new ArrayList<>();
            // then
            for (int i = 0; i < 10; i++) {
                long a = i;
                taskRepository.findById(a).ifPresent(tasks::add);
            }

        }

    }

}
