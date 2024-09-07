package gymmi.service;

import gymmi.Fixtures;
import gymmi.entity.User;
import gymmi.request.CreatingWorkspaceRequest;
import gymmi.request.MissionRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static gymmi.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class WorkspaceServiceTest {

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    EntityManager entityManager;

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
        Long result = workspaceService.createWorkspace(user, request);

        // then
        assertThat(result).isNotNull();
    }

    @Nested
    class 워크스페이스_나가기 {
        @Test
        void 방장이_방을_나가면_미션과_워크스페이스는_삭제된다() {
            // given

            // when

            // then
        }

        @Test
        void 워크스페이스를_나가면_등록한_테스크는_삭제된다() {
            // given

            // when

            // then

        }
    }

}
