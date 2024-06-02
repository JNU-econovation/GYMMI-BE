package gymmi.integration;

import gymmi.Fixtures;
import gymmi.request.RegistrationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static gymmi.Fixtures.*;
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

    @Test
    void 중복_검사_타입이_잘못된_경우_실패한다_404() {
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
                .queryParam(PARAMETER_KEY_TYPE, "1234")
                .queryParam(PARAMETER_KEY_VALUE, step.getLoginId())
                .when().get("/check-duplication");

        // when
        response.then()
                .statusCode(404)
                .body(JSON_KEY_MESSAGE, Matchers.equalTo("해당 type은 존재하지 않는 type 입니다."));
    }


    @Test
    void 닉네임_중복_검사를_성공한다_200() {
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
                .queryParam(PARAMETER_KEY_TYPE, "NICKNAME")
                .queryParam(PARAMETER_KEY_VALUE, step.getNickname())
                .when().get("/check-duplication");

        Response response1 = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .queryParam(PARAMETER_KEY_TYPE, "NICKNAME")
                .queryParam(PARAMETER_KEY_VALUE, "닉네임")
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
