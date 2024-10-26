package gymmi.workspace.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Instancio.gen;

import gymmi.exceptionhandler.message.ErrorCode;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MissionTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 16})
    void 미션_점수의_범위가_벗어나는_경우_예외가_발생한다(int score) {
        // given
        Workspace workspace = Instancio.create(Workspace.class);

        // when, then
        assertThatThrownBy(() -> new Mission(workspace, gen().string().get(), score))
                .hasMessage(ErrorCode.INVALID_WORKSPACE_MISSION_SCORE.getMessage());
    }

    @Test
    void 미션_제목의_최대_글자수를_넘어가는_경우_예외가_발생한다() {
        // given
        Workspace workspace = Instancio.create(Workspace.class);
        String missionName = Instancio.gen().string().length(Mission.MAX_NAME_LENGTH + 1).get();

        // when, then
        assertThatThrownBy(() -> new Mission(workspace, missionName, Mission.MIN_SCORE))
                .hasMessage(ErrorCode.INVALID_WORKSPACE_MISSION_NAME_LENGTH.getMessage());
    }

}
