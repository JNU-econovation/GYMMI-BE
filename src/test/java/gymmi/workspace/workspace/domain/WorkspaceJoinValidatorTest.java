package gymmi.workspace.workspace.domain;

import org.junit.jupiter.api.Test;

import static gymmi.global.exceptionhandler.message.ErrorCode.EXCEED_MAX_JOINED_WORKSPACE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class WorkspaceJoinValidatorTest {

    @Test
    void 참여하면서_완료_되지_않은_워크스페이스가_5개_이상_인_경우_예외가_발생한다() {
        // when, then
        assertThatThrownBy(() -> WorkspaceJoinValidator.validateWorkspaceCountLimit(5))
                .hasMessage(EXCEED_MAX_JOINED_WORKSPACE.getMessage());
    }

    @Test
    void 참여하면서_완료_되지_않은_워크스페이스가_4개_이하_이면_정상_통과_한다() {
        // when, then
        assertDoesNotThrow(() -> WorkspaceJoinValidator.validateWorkspaceCountLimit(4));
    }

}
