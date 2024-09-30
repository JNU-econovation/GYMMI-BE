package gymmi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gymmi.Fixtures;
import gymmi.entity.User;
import gymmi.workspace.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserRepositoryTest extends RepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Test
    void 해당_닉네임을_가진_사용자_존재_여부를_확인한다() {
        // given
        User user = User.builder()
                .loginId(Fixtures.USER__SATISFIED_LOGIN_ID)
                .plainPassword(Fixtures.USER__SATISFIED_PASSWORD)
                .nickname(Fixtures.USER__SATISFIED_NICKNAME)
                .build();
        userRepository.save(user);

        // when
        boolean result = userRepository.existsBy(Fixtures.USER__SATISFIED_NICKNAME);
        // limit ? 이 안뜨는데

        // then
        assertThat(result).isTrue();
    }

}
