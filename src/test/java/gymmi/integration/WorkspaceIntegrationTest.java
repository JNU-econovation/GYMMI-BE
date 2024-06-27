package gymmi.integration;

import gymmi.exception.AlreadyExistException;
import gymmi.exception.InvalidStateException;
import gymmi.exception.NotMatchedException;
import gymmi.request.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static gymmi.Fixtures.*;
import static gymmi.integration.Steps.*;

public class WorkspaceIntegrationTest extends IntegrationTest {

    String defaultUserToken;
    String user1Token;

    @BeforeEach
    void 인증_인가() {
        RegistrationRequest request = 회원_가입__DEFAULT_USER_REQUEST;
        this.defaultUserToken = 회원가입_및_로그인_요청(request);
        RegistrationRequest request1 = 회원_가입__USER_1_REQUEST;
        this.user1Token = 회원가입_및_로그인_요청(request1);
    }

    @Nested
    class 워크스페이스_생성 {

        @Test
        void 워크스페이스를_생성을_성공한다_200() {
            // given
            CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
                    .name(WORKSPACE__SATISFIED_NAME)
                    .task(TASK__DEFAULT_TASK)
                    .missionBoard(List.of(new MissionDTO(MISSION__SATISFIED_MISSION_NAME, MISSION__SATISFIED_MISSION_SCORE)))
                    .build();

            // when
            Response response = 워크스페이스_생성_요청(defaultUserToken, request);

            // then
            response.then()
                    .statusCode(200)
                    .body(JSON_KEY_ID, Matchers.notNullValue());
        }

        @Test
        void 워크스페이스_이름이_이미_존재하는_경우_실패한다_400() {
            // given
            CreatingWorkspaceRequest step = CreatingWorkspaceRequest.builder()
                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
                    .name(WORKSPACE__SATISFIED_NAME)
                    .task(TASK__DEFAULT_TASK)
                    .missionBoard(List.of(new MissionDTO(MISSION__SATISFIED_MISSION_NAME, MISSION__SATISFIED_MISSION_SCORE)))
                    .build();

            CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
                    .name(WORKSPACE__SATISFIED_NAME)
                    .task(TASK__DEFAULT_TASK)
                    .missionBoard(List.of(new MissionDTO(MISSION__SATISFIED_MISSION_NAME, MISSION__SATISFIED_MISSION_SCORE)))
                    .build();
            워크스페이스_생성_요청(defaultUserToken, step);

            // when
            Response response = 워크스페이스_생성_요청(defaultUserToken, request);

            // then
            response.then()
                    .statusCode(400);
        }
    }

    @Nested
    class 워크스페이스_비밀번호_보기 {

        @Test
        void 워크스페이스의_비밀번호를_확인한다() {
            // given
            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
            long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
                    .jsonPath()
                    .getLong(JSON_KEY_ID);

            // when
            Response response = 워크스페이스_비밀번호_보기_요청(defaultUserToken, workspaceId);

            // then
            response.then()
                    .statusCode(200);
        }

        @Test
        void 워크스페이스_참여자가_아닌_경우_실패한다_403() {
            // given
            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
            long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
                    .jsonPath()
                    .getLong(JSON_KEY_ID);

            // when
            Response response = 워크스페이스_비밀번호_보기_요청(user1Token, workspaceId);

            // then
            response.then()
                    .statusCode(403);
        }
    }

    @Nested
    class 워크스페이스_참여 {

        @Test
        void 워크스페이스_참여에_성공한다_200() {
            // given
            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;

            long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
                    .jsonPath()
                    .getLong(JSON_KEY_ID);

            String password = 워크스페이스_비밀번호_보기_요청(defaultUserToken, workspaceId)
                    .jsonPath()
                    .getString(JSON_KEY_PASSWORD);

            JoiningWorkspaceRequest request = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);

            // when
            Response response = 워크스페이스_참여_요청(user1Token, workspaceId, request);

            // then
            response.then()
                    .statusCode(200);
        }

        @Test
        void 워크스페이스에_이미_참여한_경우_실패한다_400() {
            // given
            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;

            long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
                    .jsonPath()
                    .getLong(JSON_KEY_ID);

            String password = 워크스페이스_비밀번호_보기_요청(defaultUserToken, workspaceId)
                    .jsonPath()
                    .getString(JSON_KEY_PASSWORD);

            JoiningWorkspaceRequest request = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);

            워크스페이스_참여_요청(user1Token, workspaceId, request);

            // when
            Response response = 워크스페이스_참여_요청(user1Token, workspaceId, request);

            // then
            response.then()
                    .statusCode(400)
                    .body(JSON_KEY_ERROR_CODE, Matchers.equalTo(AlreadyExistException.ERROR_CODE));
        }

        @Test
        void 워크스페이스_인원이_꽉_찬_경우_실패한다_400() {
            // given
            CreatingWorkspaceRequest step = CreatingWorkspaceRequest.builder()
                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
                    .headCount(2)
                    .name(WORKSPACE__SATISFIED_NAME)
                    .task(TASK__DEFAULT_TASK)
                    .missionBoard(List.of(new MissionDTO(MISSION__SATISFIED_MISSION_NAME, MISSION__SATISFIED_MISSION_SCORE)))
                    .build();

            Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
                    .jsonPath()
                    .getLong(JSON_KEY_ID);

            String password = 워크스페이스_비밀번호_보기_요청(defaultUserToken, workspaceId)
                    .jsonPath()
                    .getString(JSON_KEY_PASSWORD);

            JoiningWorkspaceRequest step1 = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
            워크스페이스_참여_요청(user1Token, workspaceId, step1);

            RegistrationRequest step2 = 회원_가입__USER_2_REQUEST;
            String user2Token = 회원가입_및_로그인_요청(step2);

            // when
            Response response = 워크스페이스_참여_요청(user2Token, workspaceId, step1);

            // then
            response.then()
                    .statusCode(400)
                    .body(JSON_KEY_ERROR_CODE, Matchers.equalTo(InvalidStateException.ERROR_CODE));
        }

        @Disabled
        @Test
        void 워크스페이스가_준비중이_아닌_경우_실패한다_400() {
            // given

            // when

            // then
        }

        @Test
        void 비밀번호가_일치하지_않는_경우_실패한다_400() {
            // given
            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;

            long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
                    .jsonPath()
                    .getLong(JSON_KEY_ID);

            JoiningWorkspaceRequest request = new JoiningWorkspaceRequest(WORKSPACE__DISSATISFIED_PASSWORD, TASK__DEFAULT_TASK);

            // when
            Response response = 워크스페이스_참여_요청(user1Token, workspaceId, request);

            // then
            response.then()
                    .statusCode(400)
                    .body(JSON_KEY_ERROR_CODE, Matchers.equalTo(NotMatchedException.ERROR_CODE));
        }
    }

    @Test
    void 워크스페이스의_비밀번호_일치_여부_확인을_성공한다_200() {
        // given
        CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;

        long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
                .jsonPath()
                .getLong(JSON_KEY_ID);

        MatchingWorkspacePasswordRequest request = new MatchingWorkspacePasswordRequest(WORKSPACE__DISSATISFIED_PASSWORD);

        // when
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + defaultUserToken)
                .pathParam("workspaceId", workspaceId)
                .body(request)
                .when().get("/workspaces/{workspaceId}/match-password");

        // then
        response.then()
                .statusCode(200)
                .body("sameness", Matchers.equalTo(false));
    }

    @Test
    void 참여한_워크스페이스_목록_확인을_성공한다_200() {
        // given
        CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;

        워크스페이스_생성_요청(defaultUserToken, step);

        // when
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + defaultUserToken)
                .queryParam("page", 0)
                .when().get("/workspaces/my");

        // then
        response.then().log().all()
                .body("[0]", Matchers.notNullValue());
    }

    @Test
    void 모든_워크스페이스_목록_확인을_성공한다_200() {
        // given
        CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;

        워크스페이스_생성_요청(defaultUserToken, step);

        // when
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + defaultUserToken)
                .queryParam("page", 0)
                .when().get("/workspaces");

        // then
        response.then().log().all()
                .body("[0]", Matchers.notNullValue());
    }

}
