package gymmi.workspace.domain;

import static gymmi.workspace.domain.WorkspaceWithMissionsConsistencyValidator.validateConsistencyMissionsCount;
import static gymmi.workspace.domain.WorkspaceWithMissionsConsistencyValidator.validateRegistration;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.exceptionhandler.message.ErrorCode;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

class WorkspaceWithMissionsConsistencyValidatorTest {

    @Test
    void 워크스페이스에_등록되지_않은_미션이_존재하는_경우_예외가_발생한다() {
        // given
        Workspace workspace = Instancio.of(Workspace.class)
                .create();
        Mission mission = Instancio.of(Mission.class)
                .filter(Select.field(Mission::getWorkspace), (Workspace ws) -> !ws.equals(workspace))
                .create();

        // when, then
        assertThatThrownBy(() -> validateRegistration(workspace, List.of(mission)))
                .hasMessage(ErrorCode.EXIST_NOT_REGISTERED_MISSION.getMessage());
    }

    @Test
    void 미션_개수의_정합성이_맞지_않는_경우_예외가_발생한다() {
        // given
        Workspace workspace = Instancio.of(Workspace.class)
                .create();
        List<Mission> missions = getMissions(workspace, WorkspaceInitializer.MAX_MISSIONS_SIZE + 1);

        // when, then
        assertThatThrownBy(() -> validateConsistencyMissionsCount(missions))
                .hasMessage(ErrorCode.NOT_CONSISTENT_MISSIONS_COUNT.getMessage());

    }

    private List<Mission> getMissions(Workspace workspace, int size) {
        List<Mission> missions = Instancio.ofList(Mission.class)
                .size(size)
                .set(Select.field(Mission::getWorkspace), workspace)
                .generate(Select.field(Mission::getScore),
                        gen -> gen.ints().range(Mission.MIN_SCORE, Mission.MAX_SCORE))
                .withUnique(Select.field(Mission::getId))
                .create();
        return missions;
    }

}
