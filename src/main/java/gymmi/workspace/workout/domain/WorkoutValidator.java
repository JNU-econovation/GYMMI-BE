package gymmi.workspace.workout.domain;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workout.repository.WorkoutHistoryRepository;
import gymmi.workspace.workspace.domain.ParticipantValidator;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkoutValidator {

    public static final int MAXIMUM_DAILY_WORKOUT_COUNT = 3;

    private final WorkoutHistoryRepository workoutHistoryRepository;

    public void validateCanWork(Workspace workspace, Worker worker) {
        ParticipantValidator.validateParticipant(workspace, worker);
        validateWorkspaceIsInProgress(workspace);
        validateDailyWorkoutHistoryCount(worker.getId());
    }

    private void validateDailyWorkoutHistoryCount(Long workerId) {
        List<WorkoutHistory> workoutHistories = workoutHistoryRepository.findTodayByWorkerId(workerId);
        if (workoutHistories.size() >= MAXIMUM_DAILY_WORKOUT_COUNT) {
            throw new InvalidStateException(ErrorCode.EXCEED_MAX_DAILY_WORKOUT_HISTORY_COUNT);
        }
    }

    private void validateWorkspaceIsInProgress(Workspace workspace) {
        if (!workspace.isInProgress()) {
            throw new InvalidStateException(ErrorCode.INACTIVE_WORKSPACE);
        }
    }

}
