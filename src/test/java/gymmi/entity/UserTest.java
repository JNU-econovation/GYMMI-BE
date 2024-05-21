package gymmi.entity;

import gymmi.exception.InvalidPatternException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @ParameterizedTest
    @ValueSource(strings = {"닉네임1!", "``"})
    void 닉네임에_불가능한_문자열이_있는_경우_예외가_발생한다(String nickname) {
        // given, when, then
        assertThatThrownBy(() -> User.builder()
                .loginId("test1")
                .plainPassword("1234")
                .nickname(nickname)
                .email(null)
                .build()).isInstanceOf(InvalidPatternException.class)
                .hasMessage("닉네임은 한글(초성), 영문, 숫자만 가능합니다.");
    }

}
