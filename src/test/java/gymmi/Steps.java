package gymmi;

import gymmi.request.LoginRequest;
import gymmi.request.RegistrationRequest;
import gymmi.request.ReissueRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public final class Steps {

    private Steps() {
    }

    public static Response 회원_가입_요청(RegistrationRequest request) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/auth/join");
        return response;
    }

    public static Response 로그인_요청(LoginRequest request) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/auth/welcome");
        return response;
    }

    public static Response 재발급_요청(ReissueRequest request) {
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/auth/reissue");
        return response;
    }
}
