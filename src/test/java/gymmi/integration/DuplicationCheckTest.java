package gymmi.integration;

import gymmi.Fixtures;
import gymmi.request.RegistrationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static gymmi.Fixtures.PARAMETER_KEY_TYPE;
import static gymmi.Fixtures.PARAMETER_KEY_VALUE;
import static gymmi.Steps.회원_가입_요청;

public class DuplicationCheckTest extends IntegrationTest {

    @Test
    void 로그인_id_중복_검사를_성공한다_200() {
        // given
        RegistrationRequest step = RegistrationRequest.builder()
                .loginId(Fixtures.SATISFIED_LOGIN_ID)
                .password(Fixtures.SATISFIED_PASSWORD)
                .nickname(Fixtures.SATISFIED_NICKNAME)
                .email(null)
                .build();

        회원_가입_요청(step);

        // when
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .queryParam(PARAMETER_KEY_TYPE, "LOGIN-ID")
                .queryParam(PARAMETER_KEY_VALUE, step.getLoginId())
                .when().get("/check-duplication");

        Response response1 = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .queryParam(PARAMETER_KEY_TYPE, "LOGIN-ID")
                .queryParam(PARAMETER_KEY_VALUE, "abcd1234")
                .when().get("/check-duplication");

        // then
        response.then()
                .statusCode(200)
                .body("duplication", Matchers.is(true));

        response1.then()
                .statusCode(200)
                .body("duplication", Matchers.is(false));
    }

}
