package gymmi;

import gymmi.request.RegistrationRequest;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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

    }
}
