package gymmi.workspace.domain.entity;

import gymmi.workspace.workout.domain.WorkoutValidator;
import org.junit.jupiter.api.Test;

import static gymmi.global.exception.message.ErrorCode.EXCEED_MAX_DAILY_WORKOUT_HISTORY_COUNT;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkoutValidatorTest {

    @Test
    void 워크스페이스_일일_기록이_횟수가_최대에_도달한_경우_예외가_발생한다() {
        // when, then
        assertThatThrownBy(() -> WorkoutValidator.validateDailyWorkoutHistoryCount(3))
                .hasMessage(EXCEED_MAX_DAILY_WORKOUT_HISTORY_COUNT.getMessage());

    }

}
