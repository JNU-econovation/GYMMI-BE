package gymmi.workspace.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class WorkspacePhaseTest {

    @ParameterizedTest
    @CsvSource(value = {"100,49,25", "1000,501,50", "100,1,0", "1000,756,75"})
    void 워크스페이스_페이즈를_가져온다(int goalScore, int achievementScore, int result) {
        // when
        WorkspacePhase workspacePhase = WorkspacePhase.from(goalScore, achievementScore);

        // then
        assertThat(workspacePhase.getValue()).isEqualTo(result);
    }

}
