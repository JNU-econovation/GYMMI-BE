package gymmi;

import gymmi.request.RegistrationRequest;
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
}
