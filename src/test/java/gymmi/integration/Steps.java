package gymmi.integration;

import gymmi.Fixtures;
import gymmi.request.CreatingWorkspaceRequest;
import gymmi.request.LoginRequest;
import gymmi.request.RegistrationRequest;
import gymmi.request.ReissueRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;

import static gymmi.Fixtures.AUTHORIZATION_TYPE_BEARER;
import static gymmi.Fixtures.JSON_KEY_ACCESS_TOKEN;

public final class Steps {

    public static final RegistrationRequest DEFAULT_REQUEST = RegistrationRequest.builder()
            .loginId(Fixtures.SATISFIED_LOGIN_ID)
            .password(Fixtures.SATISFIED_PASSWORD)
            .nickname(Fixtures.SATISFIED_NICKNAME)
            .email(null)
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

    public static Response 워크스페이스_생성_요청(CreatingWorkspaceRequest request, String accessToken) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + accessToken)
                .body(request)
                .when().post("/workspaces");
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
