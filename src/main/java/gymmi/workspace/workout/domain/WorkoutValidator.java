package gymmi.workspace.workout.domain;

import gymmi.global.exception.exceptiontype.InvalidStateException;
import gymmi.global.exception.message.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorkoutValidator {

    public static final int MAXIMUM_DAILY_WORKOUT_COUNT = 3;

    public static void validateDailyWorkoutHistoryCount(int todayWorkoutCount) {
        if (todayWorkoutCount >= MAXIMUM_DAILY_WORKOUT_COUNT) {
            throw new InvalidStateException(ErrorCode.EXCEED_MAX_DAILY_WORKOUT_HISTORY_COUNT);
        }
    }

}
