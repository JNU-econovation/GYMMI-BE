package gymmi.workspace.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Instancio.gen;

import gymmi.exceptionhandler.message.ErrorCode;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.Select;
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

    @Test
    void 워크스페이스에_등록되지_않은_미션_이면_예외가_발생_한다() {
        // given
        List<Workspace> workspaces = Instancio.ofList(Workspace.class)
                .size(2)
                .withUnique(Select.field(Workspace::getId))
                .create();
        Mission mission = Instancio.of(Mission.class)
                .set(Select.field(Mission::getWorkspace), workspaces.get(0))
                .create();

        // when, then
        assertThatThrownBy(() -> mission.canBeReadIn(workspaces.get(1)))
                .hasMessage(ErrorCode.NOT_REGISTERED_WORKSPACE_MISSION.getMessage());
    }

}
