package gymmi.entity;

import static gymmi.Fixtures.WORKSPACE__SATISFIED_GOAL_SCORE;
import static gymmi.Fixtures.WORKSPACE__SATISFIED_HEAD_COUNT;
import static gymmi.Fixtures.WORKSPACE__SATISFIED_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.exceptionhandler.exception.InvalidNumberException;
import gymmi.exceptionhandler.exception.InvalidPatternException;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.domain.WorkspaceStatus;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

class WorkspaceTest {

    @Test
    void 워크스페이스를_생성한다() {
        // give
        User user = Instancio.create(User.class);

        // when
        Workspace workspace = Workspace.builder()
                .creator(user)
                .name(WORKSPACE__SATISFIED_NAME)
                .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
                .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
                .description(null)
                .tag(null)
                .build();

        // then
        assertThat(workspace.getPassword().length()).isEqualTo(4);
        assertThat(workspace.getStatus()).isEqualTo(WorkspaceStatus.PREPARING);
    }

    @Test
    void 목표_점수가_10점_단위가_아닌_경우_예외가_발생한다() {
        // given
        User user = Instancio.create(User.class);

        // when, then
        assertThatThrownBy(() -> Workspace.builder()
                .creator(user)
                .name(WORKSPACE__SATISFIED_NAME)
                .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
                .goalScore(505)
                .description(null)
                .tag(null)
                .build())
                .isInstanceOf(InvalidNumberException.class);
    }

    @Test
    void 태그에_한글과_영어가_아닌_문자가_들어간_경우_예외가_발생한다() {
        // given
        User user = Instancio.create(User.class);

        // when, then
        assertThatThrownBy(() -> Workspace.builder()
                .creator(user)
                .name(WORKSPACE__SATISFIED_NAME)
                .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
                .goalScore(510)
                .description(null)
                .tag("123")
                .build())
                .isInstanceOf(InvalidPatternException.class);
    }

    @Test
    void 이름에_한글과_영어와_숫자가_아닌_문자가_들어간_경우_예외가_발생한다() {
        // given
        User user = Instancio.create(User.class);

        // when, then
        assertThatThrownBy(() -> Workspace.builder()
                .creator(user)
                .name("ㄱㄴㄷㄹㅁㅂ")
                .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
                .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
                .description(null)
                .tag(null)
                .build())
                .isInstanceOf(InvalidPatternException.class);
    }
}



