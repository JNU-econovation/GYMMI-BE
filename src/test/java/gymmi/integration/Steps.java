package gymmi.integration;

import static gymmi.Fixtures.AUTHORIZATION_TYPE_BEARER;
import static gymmi.Fixtures.JSON_KEY_ACCESS_TOKEN;
import static gymmi.Fixtures.MISSION__SATISFIED_MISSION_NAME;
import static gymmi.Fixtures.MISSION__SATISFIED_MISSION_SCORE;
import static gymmi.Fixtures.TASK__DEFAULT_TASK;
import static gymmi.Fixtures.USER__SATISFIED_PASSWORD;
import static gymmi.Fixtures.WORKSPACE__SATISFIED_GOAL_SCORE;
import static gymmi.Fixtures.WORKSPACE__SATISFIED_HEAD_COUNT;
import static gymmi.Fixtures.WORKSPACE__SATISFIED_NAME;

import gymmi.Fixtures;
import gymmi.request.CreatingWorkspaceRequest;
import gymmi.request.EditingDescriptionOfWorkspaceRequest;
import gymmi.request.JoiningWorkspaceRequest;
import gymmi.request.LoginRequest;
import gymmi.request.MissionDTO;
import gymmi.request.RegistrationRequest;
import gymmi.request.ReissueRequest;
import gymmi.request.WorkingMissionInWorkspaceRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpHeaders;

public final class Steps {

    public static final RegistrationRequest 회원_가입__DEFAULT_USER_REQUEST = RegistrationRequest.builder()
            .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
            .password(Fixtures.USER__SATISFIED_PASSWORD)
            .nickname(Fixtures.USER__SATISFIED_NICKNAME)
            .email(null)
            .build();

    public static final RegistrationRequest 회원_가입__USER_1_REQUEST = RegistrationRequest.builder()
            .loginId("useruser1")
            .password(USER__SATISFIED_PASSWORD)
            .nickname("user1")
            .email(null)
            .build();

    public static final RegistrationRequest 회원_가입__USER_2_REQUEST = RegistrationRequest.builder()
            .loginId("useruser2")
            .password(USER__SATISFIED_PASSWORD)
            .nickname("user2")
            .email(null)
            .build();

    public static final CreatingWorkspaceRequest 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST = CreatingWorkspaceRequest.builder()
            .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
            .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
            .name(WORKSPACE__SATISFIED_NAME)
            .task(TASK__DEFAULT_TASK)
            .missionBoard(List.of(new MissionDTO(MISSION__SATISFIED_MISSION_NAME, MISSION__SATISFIED_MISSION_SCORE)))
            .build();


    private Steps() {
    }

    public static Response 회원_가입_요청(RegistrationRequest request) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/auth/join");
        response.then().log().all();
        return response;
    }

    public static Response 로그인_요청(LoginRequest request) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/auth/welcome");
        response.then().log().all();
        return response;
    }

    public static Response 재발급_요청(ReissueRequest request) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/auth/reissue");
        response.then().log().all();
        return response;
    }

    public static Response 로그아웃_요청(String accessToken) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + accessToken)
                .when().post("/auth/goodbye");
        response.then().log();
        return response;
    }

    public static Response 워크스페이스_생성_요청(String accessToken, CreatingWorkspaceRequest request) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + accessToken)
                .body(request)
                .when().post("/workspaces");
        response.then().log().all();
        return response;
    }

    public static Response 워크스페이스_소개_보기_요청(String accessToken, Long workspaceId) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + accessToken)
                .pathParam("workspaceId", workspaceId)
                .when().get("/workspaces/{workspaceId}/introduction");
        response.then().log().all();
        return response;
    }

    public static Response 워크스페이스_참여_요청(String accessToken, Long workspaceId, JoiningWorkspaceRequest request) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + accessToken)
                .pathParam("workspaceId", workspaceId)
                .body(request)
                .when().post("/workspaces/{workspaceId}/join");
        response.then().log().all();
        return response;
    }

    public static Response 워크스페이스_시작_요청(String accessToken, Long workspaceId) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + accessToken)
                .pathParam("workspaceId", workspaceId)
                .when().patch("/workspaces/{workspaceId}/start");
        response.then().log().all();
        return response;
    }

    public static Response 워크스페이스_나가기_요청(String accessToken, Long workspaceId) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + accessToken)
                .pathParam("workspaceId", workspaceId)
                .when().post("/workspaces/{workspaceId}/leave");
        response.then().log().all();
        return response;
    }

    public static Response 워크스페이스_미션_보기_요청(String accessToken, Long workspaceId) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + accessToken)
                .pathParam("workspaceId", workspaceId)
                .when().get("/workspaces/{workspaceId}/missions");
        response.then().log().all();
        return response;
    }

    public static Response 워크스페이스_입장_요청(String accessToken, Long workspaceId) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + accessToken)
                .pathParam("workspaceId", workspaceId)
                .when().get("/workspaces/{workspaceId}");
        response.then().log().all();
        return response;
    }

    public static Response 미션_수행_요청(
            String accessToken, Long workspaceId, List<WorkingMissionInWorkspaceRequest> requests
    ) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + accessToken)
                .pathParam("workspaceId", workspaceId)
                .body(requests)
                .when().post("/workspaces/{workspaceId}/missions");
        response.then().log().all();
        return response;
    }

    public static Response 워크스페이스_설명_수정_요청(
            String accessToken, Long workspaceId, EditingDescriptionOfWorkspaceRequest request
    ) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + accessToken)
                .pathParam("workspaceId", workspaceId)
                .body(request)
                .when().put("/workspaces/{workspaceId}/edit");
        response.then().log().all();
        return response;
    }

    public static String 회원가입_및_로그인_요청(RegistrationRequest request) {
        회원_가입_요청(request);
        LoginRequest loginRequest = new LoginRequest(request.getLoginId(), request.getPassword());
        return 로그인_요청(loginRequest)
                .jsonPath()
                .getString(JSON_KEY_ACCESS_TOKEN);
    }
}
