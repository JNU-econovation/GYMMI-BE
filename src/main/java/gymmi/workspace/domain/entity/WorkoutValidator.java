package gymmi.workspace.domain.entity;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.repository.WorkoutHistoryRepository;
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
