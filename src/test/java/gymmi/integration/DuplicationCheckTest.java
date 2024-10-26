package gymmi.integration;

import static gymmi.integration.Steps.회원_가입_요청;

import gymmi.Fixtures;
import gymmi.workspace.request.RegistrationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class DuplicationCheckTest extends EndToEndTest {

    private static final String TYPE = "type";
    private static final String VALUE = "value";
    public static final String DUPLICATION = "duplication";

    @Test
    void 로그인_id_중복_검사를_성공한다_200() {
        // given
        RegistrationRequest step = RegistrationRequest.builder()
                .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                .password(Fixtures.USER__SATISFIED_PASSWORD)
                .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                .email(null)
                .build();

        회원_가입_요청(step);

        // when
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .queryParam(TYPE, "LOGIN-ID")
                .queryParam(VALUE, step.getLoginId())
                .when().get("/check-duplication");

        Response response1 = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .queryParam(TYPE, "LOGIN-ID")
                .queryParam(VALUE, "abcd1234")
                .when().get("/check-duplication");

        // then
        response.then()
                .statusCode(200)
                .body(DUPLICATION, Matchers.is(true));

        response1.then()
                .statusCode(200)
                .body(DUPLICATION, Matchers.is(false));
    }

    @Disabled
    @Test
    void 중복_검사_타입이_잘못된_경우_실패한다_404() {
        // given
        RegistrationRequest step = RegistrationRequest.builder()
                .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                .password(Fixtures.USER__SATISFIED_PASSWORD)
                .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                .email(null)
                .build();

        회원_가입_요청(step);

        // when
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .queryParam(TYPE, "1234")
                .queryParam(VALUE, step.getLoginId())
                .when().get("/check-duplication");

        // when
        response.then().log().all()
//                .statusCode(404)
                .statusCode(600);
//                .body(JSON_KEY_MESSAGE, Matchers.equalTo("해당 type은 존재하지 않는 type 입니다."));
    }


    @Test
    void 닉네임_중복_검사를_성공한다_200() {
        // given
        RegistrationRequest step = RegistrationRequest.builder()
                .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                .password(Fixtures.USER__SATISFIED_PASSWORD)
                .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                .email(null)
                .build();

        회원_가입_요청(step);

        // when
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .queryParam(TYPE, "NICKNAME")
                .queryParam(VALUE, step.getNickname())
                .when().get("/check-duplication");

        Response response1 = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .queryParam(TYPE, "NICKNAME")
                .queryParam(VALUE, "닉네임")
                .when().get("/check-duplication");

        // then
        response.then()
                .statusCode(200)
                .body(DUPLICATION, Matchers.is(true));

        response1.then()
                .statusCode(200)
                .body(DUPLICATION, Matchers.is(false));
    }
}
