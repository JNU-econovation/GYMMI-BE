package gymmi.integration;

import gymmi.request.RegistrationRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static gymmi.integration.Steps.*;

public class MyPageIntegrationTest extends IntegrationTest {

    String defaultUserToken;
    String user1Token;

    @BeforeEach
    void 인증_인가() {
        RegistrationRequest request = 회원_가입__DEFAULT_USER_REQUEST;
        this.defaultUserToken = 회원가입_및_로그인_요청(request);
        RegistrationRequest request1 = 회원_가입__USER_1_REQUEST;
        this.user1Token = 회원가입_및_로그인_요청(request1);
    }

    @Disabled
    @Test
    void 프로필_사진_업로드를_성공한다_200() {
        // given
        Response 프로필_이미지_설정_요청 = 프로필_이미지_설정_요청(defaultUserToken, null);
        // when

        // then

    }

}
