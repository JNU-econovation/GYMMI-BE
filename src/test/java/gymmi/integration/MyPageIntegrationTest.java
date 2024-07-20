package gymmi.integration;

import gymmi.request.EditingMyPageRequest;
import gymmi.request.RegistrationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static gymmi.Fixtures.AUTHORIZATION_TYPE_BEARER;
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

    @Test
    void 닉네임_수정을_성공한다_200() {
        // given, when
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + defaultUserToken)
                .body(new EditingMyPageRequest("123"))
                .when().put("/my/nickname/edit");
        response.then().log().all();

        // then
        response.then()
                .statusCode(200);
    }

}
