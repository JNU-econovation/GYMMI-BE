package gymmi.integration;

import static gymmi.Fixtures.AUTHORIZATION_TYPE_BEARER;
import static gymmi.Fixtures.JSON_KEY_ACCESS_TOKEN;
import static gymmi.Fixtures.JSON_KEY_MESSAGE;
import static gymmi.Fixtures.JSON_KEY_REFRESH_TOKEN;
import static gymmi.integration.Steps.로그아웃_요청;
import static gymmi.integration.Steps.로그인_요청;
import static gymmi.integration.Steps.재발급_요청;
import static gymmi.integration.Steps.회원_가입_요청;

import gymmi.Fixtures;
import gymmi.request.LoginRequest;
import gymmi.request.RegistrationRequest;
import gymmi.request.ReissueRequest;
import gymmi.service.TokenProcessor;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

public class AuthTest extends IntegrationTest {

    @Nested
    class 회원_가입 {

        @Test
        void 회원_가입을_성공한다_200() {
            // given
            RegistrationRequest request = RegistrationRequest.builder()
                    .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                    .password(Fixtures.USER__SATISFIED_PASSWORD)
                    .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                    .email(null)
                    .build();

            // when
            Response response = 회원_가입_요청(request);

            // then
            response.then()
                    .statusCode(200);
        }

        @Test
        void 이미_존재하는_아이디인_경우_실패한다_400() {
            // given
            RegistrationRequest step = RegistrationRequest.builder()
                    .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                    .password(Fixtures.USER__SATISFIED_PASSWORD)
                    .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                    .email(null)
                    .build();

            RegistrationRequest request = step;

            회원_가입_요청(step);

            // when
            Response response = 회원_가입_요청(request);

            // then
            response.then()
                    .statusCode(400)
                    .body(JSON_KEY_MESSAGE, Matchers.equalTo("이미 등록된 아이디 입니다."));
        }
    }

    @Nested
    class 로그인 {

        @Test
        void 로그인을_성공한다_200() {
            // given
            RegistrationRequest step = RegistrationRequest.builder()
                    .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                    .password(Fixtures.USER__SATISFIED_PASSWORD)
                    .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                    .email(null)
                    .build();

            회원_가입_요청(step);

            LoginRequest request = new LoginRequest(step.getLoginId(), step.getPassword());

            // when
            Response response = 로그인_요청(request);

            // then
            response.then().log().all()
                    .statusCode(200)
                    .body(
                            JSON_KEY_ACCESS_TOKEN, Matchers.notNullValue(),
                            JSON_KEY_REFRESH_TOKEN, Matchers.notNullValue()
                    );
        }

        @Test
        void 아이디가_일치하지_않는_경우_로그인을_실패한다_400() {
            RegistrationRequest step = RegistrationRequest.builder()
                    .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                    .password(Fixtures.USER__SATISFIED_PASSWORD)
                    .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                    .email(null)
                    .build();

            회원_가입_요청(step);

            LoginRequest request = new LoginRequest("abcdefg1", step.getPassword());

            // when
            Response response = 로그인_요청(request);

            // then
            response.then()
                    .statusCode(400);

        }

        @Test
        void 비밀번호가_일치하지_않는_경우_로그인을_실패한다_400() {
            RegistrationRequest step = RegistrationRequest.builder()
                    .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                    .password(Fixtures.USER__SATISFIED_PASSWORD)
                    .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                    .email(null)
                    .build();

            회원_가입_요청(step).then().log().all();

            LoginRequest request = new LoginRequest(step.getLoginId(), "passwodasd!");

            // when
            Response response = 로그인_요청(request);

            // then
            response.then()
                    .statusCode(400);

        }
    }

    @Nested
    class 토큰_재발급 {

        @Autowired
        TokenProcessor tokenProcessor;

        @Test
        void 리프레시_토큰으로_엑세스토큰과_리프레시토큰_재발급을_성공한다_200() {
            // given
            RegistrationRequest step = RegistrationRequest.builder()
                    .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                    .password(Fixtures.USER__SATISFIED_PASSWORD)
                    .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                    .email(null)
                    .build();

            LoginRequest step1 = new LoginRequest(step.getLoginId(), step.getPassword());

            회원_가입_요청(step);
            String refreshToken = 로그인_요청(step1)
                    .jsonPath()
                    .getString(JSON_KEY_REFRESH_TOKEN);

            ReissueRequest request = new ReissueRequest(refreshToken);
            // when
            Response response = 재발급_요청(request);

            // then
            response.then()
                    .statusCode(200)
                    .body(
                            JSON_KEY_ACCESS_TOKEN, Matchers.notNullValue(),
                            JSON_KEY_REFRESH_TOKEN, Matchers.notNullValue()
                    );
        }

        @Test
        void 비활성화된_리프레시_토큰으로_엑세스토큰과_리프레시토큰을_재발급_하는_경우_실패한다_401() throws InterruptedException {
            // given
            RegistrationRequest step = RegistrationRequest.builder()
                    .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                    .password(Fixtures.USER__SATISFIED_PASSWORD)
                    .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                    .email(null)
                    .build();

            LoginRequest step1 = new LoginRequest(step.getLoginId(), step.getPassword());

            회원_가입_요청(step);
            String refreshToken = 로그인_요청(step1)
                    .jsonPath()
                    .getString(JSON_KEY_REFRESH_TOKEN);

            Thread.sleep(1000);
            String activatedRefreshToken = 재발급_요청(new ReissueRequest(refreshToken))
                    .jsonPath()
                    .getString(JSON_KEY_REFRESH_TOKEN);

            // when
            Response response = 재발급_요청(new ReissueRequest(refreshToken));

            // then
            response.then()
                    .statusCode(401)
                    .body(JSON_KEY_MESSAGE, Matchers.equalTo("잘못된 접근입니다. 다시 로그인 해주세요."));
        }
    }

    @Test
    void 로그아웃을_성공한다_200() {
        // given
        RegistrationRequest step = RegistrationRequest.builder()
                .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                .password(Fixtures.USER__SATISFIED_PASSWORD)
                .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                .email(null)
                .build();

        LoginRequest step1 = new LoginRequest(step.getLoginId(), step.getPassword());

        회원_가입_요청(step);
        String accessToken = 로그인_요청(step1)
                .jsonPath()
                .getString(JSON_KEY_ACCESS_TOKEN);

        // when
        Response response = 로그아웃_요청(accessToken);

        // then
        response.then()
                .statusCode(200);

    }

    @Nested
    class 엑세스_토큰으로_api_요청 {

        @Test
        void Authorization_헤더에_엑세스_토큰이_비어있는_경우_실패_한다_401() {
            // given
            RegistrationRequest step = RegistrationRequest.builder()
                    .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                    .password(Fixtures.USER__SATISFIED_PASSWORD)
                    .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                    .email(null)
                    .build();

            LoginRequest step1 = new LoginRequest(step.getLoginId(), step.getPassword());

            회원_가입_요청(step);
            String accessToken = 로그인_요청(step1)
                    .jsonPath()
                    .getString(JSON_KEY_ACCESS_TOKEN);

            // when
            Response response = RestAssured
                    .given().log().all()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "")
                    .when().post("/auth/goodbye");

            // then
            response.then()
                    .statusCode(401);
        }

        @Test
        void Authorization에서_타입이_Bearer이_아닌_경우_실패한다_401() {
            // given
            RegistrationRequest step = RegistrationRequest.builder()
                    .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                    .password(Fixtures.USER__SATISFIED_PASSWORD)
                    .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                    .email(null)
                    .build();

            LoginRequest step1 = new LoginRequest(step.getLoginId(), step.getPassword());

            회원_가입_요청(step);
            String accessToken = 로그인_요청(step1)
                    .jsonPath()
                    .getString(JSON_KEY_ACCESS_TOKEN);

            // when
            Response response = RestAssured
                    .given().log().all()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "" + accessToken)
                    .when().post("/auth/goodbye");

            // then
            response.then()
                    .statusCode(401);
        }

        @Test
        void 엑세스_토큰_대신_리프레시_토큰으로_요청하는_경우_실패한다_401() {
            // given
            RegistrationRequest step = RegistrationRequest.builder()
                    .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                    .password(Fixtures.USER__SATISFIED_PASSWORD)
                    .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                    .email(null)
                    .build();

            LoginRequest step1 = new LoginRequest(step.getLoginId(), step.getPassword());

            회원_가입_요청(step);
            String refreshToken = 로그인_요청(step1)
                    .jsonPath()
                    .getString(JSON_KEY_REFRESH_TOKEN);

            // when
            Response response = RestAssured
                    .given().log().all()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + refreshToken)
                    .when().post("/auth/goodbye");

            // then
            response.then()
                    .statusCode(401)
                    .body(JSON_KEY_MESSAGE, Matchers.equalTo("토큰 제목을 확인해 주세요."));
        }
    }
}
