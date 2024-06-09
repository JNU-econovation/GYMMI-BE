package gymmi.integration;

import gymmi.request.CreatingWorkspaceRequest;
import gymmi.request.MissionDTO;
import gymmi.request.RegistrationRequest;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gymmi.Fixtures.*;
import static gymmi.Fixtures.Workspace.*;
import static gymmi.integration.Steps.*;

public class WorkspaceTest extends IntegrationTest {
    // 테스트용 토큰? 테스트용 리졸버? 도입?

    String accessToken;

    @BeforeEach
    void 인증_인가() {
        RegistrationRequest request = DEFAULT_REQUEST;
        this.accessToken = 회원가입_및_로그인_요청(request);
    }

    @Test
    void 워크스페이스를_생성을_성공한다_200() {
        // given
        CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
                .goalScore(SATISFIED_GOAL_SCORE)
                .headCount(SATISFIED_HEAD_COUNT)
                .name(SATISFIED_NAME)
                .task(DEFAULT_TASK)
                .missionBoard(List.of(new MissionDTO(SATISFIED_MISSION_NAME, SATISFIED_MISSION_SCORE)))
                .build();

        // when
        Response response = 워크스페이스_생성_요청(request, accessToken);

        // then
        response.then()
                .statusCode(200)
                .body(JSON_KEY_ID, Matchers.notNullValue());
    }

    @Test
    void 워크스페이스_이름이_이미_존재하는_경우_실패한다_400() {
        // given
        CreatingWorkspaceRequest step = CreatingWorkspaceRequest.builder()
                .goalScore(SATISFIED_GOAL_SCORE)
                .headCount(SATISFIED_HEAD_COUNT)
                .name(SATISFIED_NAME)
                .task(DEFAULT_TASK)
                .missionBoard(List.of(new MissionDTO(SATISFIED_MISSION_NAME, SATISFIED_MISSION_SCORE)))
                .build();

        CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
                .goalScore(SATISFIED_GOAL_SCORE)
                .headCount(SATISFIED_HEAD_COUNT)
                .name(SATISFIED_NAME)
                .task(DEFAULT_TASK)
                .missionBoard(List.of(new MissionDTO(SATISFIED_MISSION_NAME, SATISFIED_MISSION_SCORE)))
                .build();
        워크스페이스_생성_요청(step, accessToken);

        // when
        Response response = 워크스페이스_생성_요청(request, accessToken);

        // then
        response.then()
                .statusCode(400);
    }
}

