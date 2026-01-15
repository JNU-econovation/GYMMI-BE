package gymmi.workspace.workout.domain;

import gymmi.global.exception.exceptiontype.NotFoundException;
import gymmi.global.exception.message.ErrorCode;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workspace.domain.entity.Workspace;

public class WorkoutHistoryInWorkspaceValidator {

    public static void validateWorkoutHistoryInWorkspace(Workspace workspace, WorkoutHistory workoutHistory) {
        if (!workoutHistory.isIn(workspace)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_OBJECTION);
        }
    }
}
