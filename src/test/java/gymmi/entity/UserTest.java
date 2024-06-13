package gymmi.entity;

import gymmi.exception.InvalidPatternException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static gymmi.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {


    @Test
    void 회원을_생성한다() {
        // given, when, then
        User user = User.builder()
                .loginId(USER__SATISFIED_LOGIN_ID)
                .plainPassword(USER__SATISFIED_PASSWORD)
                .nickname(USER__SATISFIED_NICKNAME)
                .email(null)
                .build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcdef", "한글사랑해요", "123456"})
    void 아이디_조건이_잘못된_경우_예외가_발생한다(String loginId) {
        // given, when, then
        assertThatThrownBy(() -> User.builder()
                .loginId(loginId)
                .plainPassword(USER__SATISFIED_PASSWORD)
                .nickname(USER__SATISFIED_NICKNAME)
                .email(null)
                .build())
                .isInstanceOf(InvalidPatternException.class)
//                .hasMessage("")
        ;
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "password1", "12345678!", "비밀번호"})
    void 비밀번호_조건이_잘못된_경우_예외가_발생한다(String password) {
        // given, when, then
        assertThatThrownBy(() -> User.builder()
                .loginId(USER__SATISFIED_LOGIN_ID)
                .plainPassword(password)
                .nickname(USER__SATISFIED_NICKNAME)
                .email(null)
                .build())
                .isInstanceOf(InvalidPatternException.class)
//                .hasMessage("")
        ;
    }

    @ParameterizedTest
    @ValueSource(strings = {"닉네임1!", "``"})
    void 닉네임_조건이_잘못된_경우_예외가_발생한다(String nickname) {
        // given, when, then
        assertThatThrownBy(() -> User.builder()
                .loginId(USER__SATISFIED_LOGIN_ID)
                .plainPassword(USER__SATISFIED_PASSWORD)
                .nickname(nickname)
                .email(null)
                .build())
                .isInstanceOf(InvalidPatternException.class)
//                .hasMessage("")
        ;
    }

}
