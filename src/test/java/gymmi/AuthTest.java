package gymmi;

import gymmi.request.LoginRequest;
import gymmi.request.RegistrationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static gymmi.Steps.로그인_요청;
import static gymmi.Steps.회원_가입_요청;

public class AuthTest extends IntegrationTest {

    @Nested
    class 회원_가입 extends IntegrationTest {

        @Test
        void 회원_가입을_성공한다_200() {
            // given
            RegistrationRequest request = RegistrationRequest.builder()
                    .loginId("asdasd1")
                    .password("12314124")
                    .nickname("닉네임")
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
                    .loginId("asdasd1")
                    .password("12314124")
                    .nickname("닉네임")
                    .email(null)
                    .build();

            RegistrationRequest request = RegistrationRequest.builder()
                    .loginId("asdasd1")
                    .password("12314124")
                    .nickname("닉네임")
                    .email(null)
                    .build();

            회원_가입_요청(step);

            // when
            Response response = 회원_가입_요청(request);

            // then
            response.then()
                    .statusCode(400)
                    .body("message", Matchers.equalTo("이미 등록된 아이디 입니다."));
        }

        @Nested
        class 로그인 extends IntegrationTest {

            @Test
            void 로그인을_성공한다_200() {
                // given
                RegistrationRequest step = RegistrationRequest.builder()
                        .loginId("gymmi1")
                        .password("password1!")
                        .nickname("지미지미")
                        .email(null)
                        .build();

                회원_가입_요청(step).then().log().all();

                LoginRequest request = new LoginRequest(step.getLoginId(), step.getPassword());

                // when
                Response response = 로그인_요청(request);

                // then
                response.then().log().all()
                        .statusCode(200)
                        .body(
                                "accessToken", Matchers.notNullValue(),
                                "refreshToken", Matchers.notNullValue()
                        );
            }
            
            @Test
            void 아이디가_일치하지_않는_경우_로그인을_실패한다_400() {
                RegistrationRequest step = RegistrationRequest.builder()
                        .loginId("gymmi1")
                        .password("password1!")
                        .nickname("지미지미")
                        .email(null)
                        .build();

                회원_가입_요청(step).then().log().all();

                LoginRequest request = new LoginRequest("abcdefg1", step.getPassword());

                // when
                Response response = 로그인_요청(request);

                // then
                response.then().log().all()
                        .statusCode(400);

            }

            @Test
            void 비밀번호가_일치하지_않는_경우_로그인을_실패한다_400() {
                RegistrationRequest step = RegistrationRequest.builder()
                        .loginId("gymmi1")
                        .password("password1!")
                        .nickname("지미지미")
                        .email(null)
                        .build();

                회원_가입_요청(step).then().log().all();

                LoginRequest request = new LoginRequest(step.getLoginId(), "passwodasd!");

                // when
                Response response = 로그인_요청(request);

                // then
                response.then().log().all()
                        .statusCode(400);

            }

        }
    }

}
