package gymmi.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.exception.AuthenticationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TokenProcessorTest {

    @Nested
    class 토큰_파싱 {

        @Test
        void 클레임인_userId를_가져온다() {
            // given
            TokenProcessor tokenProcessor = new TokenProcessor(
                    "지미의 임시 비밀번호 키 입니다 12345",
                    1000000000L,
                    100000000000L
            );
            Long userId = 1L;
            String jwt = tokenProcessor.generateAccessToken(userId);

            // when
            Long result = tokenProcessor.parseAccessToken(jwt);

            // then
            Assertions.assertThat(result).isEqualTo(userId);
        }

        @Test
        void 토큰의_종류가_다른경우_예외가_발생한다() {
            // given
            TokenProcessor tokenProcessor = new TokenProcessor(
                    "지미의 임시 비밀번호 키 입니다 12345",
                    1000000000L,
                    100000000000L
            );

            // when
            String accessToken = tokenProcessor.generateAccessToken(1L);
            String refreshToken = tokenProcessor.generateRefreshToken(1L);

            // then
            assertThatThrownBy(() -> tokenProcessor.parseAccessToken(refreshToken))
                    .isInstanceOf(AuthenticationException.class);
            assertThatThrownBy(() -> tokenProcessor.parseRefreshToken(accessToken))
                    .isInstanceOf(AuthenticationException.class);
        }

        @Test
        void 토큰이_만료된_경우_예외가_발생한다() {
            // given
            TokenProcessor tokenProcessor = new TokenProcessor(
                    "지미의 임시 비밀번호 키 입니다 12345",
                    -1000000000L,
                    -100000000000L
            );
            Long userId = 1L;
            String jwt = tokenProcessor.generateAccessToken(userId);

            // when, then
            assertThatThrownBy(() -> tokenProcessor.parseAccessToken(jwt))
                    .isInstanceOf(AuthenticationException.class);
        }

        @Test
        void 시그니쳐가_다른_경우_예외가_발생한다() {
            // given
            TokenProcessor differProcessor = new TokenProcessor(
                    "지미의 임시 비밀번호 키 입니다 12345",
                    1000000000L,
                    100000000000L
            );
            Long userId = 1L;
            String jwt = differProcessor.generateAccessToken(userId);

            TokenProcessor tokenProcessor = new TokenProcessor(
                    "12345 지미의 임시 비밀번호 키 입니다",
                    1000000000L,
                    100000000000L
            );

            // when, then
            assertThatThrownBy(() -> tokenProcessor.parseAccessToken(jwt))
                    .isInstanceOf(AuthenticationException.class);
        }
    }

}
