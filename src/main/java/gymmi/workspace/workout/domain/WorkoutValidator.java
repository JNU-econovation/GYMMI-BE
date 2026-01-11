package gymmi.workspace.workout.domain;

import gymmi.global.exceptionhandler.exception.InvalidStateException;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workout.repository.WorkoutHistoryRepository;
import gymmi.workspace.workspace.domain.ParticipantValidator;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
public class WorkoutValidator {

    public static final int MAXIMUM_DAILY_WORKOUT_COUNT = 3;

    public static void validateDailyWorkoutHistoryCount(int todayWorkoutCount) {
        if (todayWorkoutCount >= MAXIMUM_DAILY_WORKOUT_COUNT) {
            throw new InvalidStateException(ErrorCode.EXCEED_MAX_DAILY_WORKOUT_HISTORY_COUNT);
        }
    }

}
