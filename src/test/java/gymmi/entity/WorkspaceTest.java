package gymmi.entity;

import gymmi.exception.InvalidNumberException;
import gymmi.exception.InvalidPatternException;
import org.junit.jupiter.api.Test;

import static gymmi.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkspaceTest {

    // 목 사용해보기 or 테스크객체 활용 편하게하는법
    User defaultUser = USER__DEFAULT_USER;

    @Test
    void 워크스페이스를_생성한다() {
        // give, when
        Workspace workspace = Workspace.builder()
                .creator(defaultUser)
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
        // given, when, then
        assertThatThrownBy(() -> Workspace.builder()
                .creator(defaultUser)
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
        // given, when, then
        assertThatThrownBy(() -> Workspace.builder()
                .creator(defaultUser)
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
        // given, when, then
        assertThatThrownBy(() -> Workspace.builder()
                .creator(defaultUser)
                .name("ㄱㄴㄷㄹㅁㅂ")
                .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
                .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
                .description(null)
                .tag(null)
                .build())
                .isInstanceOf(InvalidPatternException.class);
    }
}



