package gymmi;

import gymmi.request.LoginRequest;
import gymmi.request.RegistrationRequest;
import gymmi.request.ReissueRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;

import static gymmi.Fixtures.AUTHORIZATION_TYPE_BEARER;

public final class Steps {

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
}
