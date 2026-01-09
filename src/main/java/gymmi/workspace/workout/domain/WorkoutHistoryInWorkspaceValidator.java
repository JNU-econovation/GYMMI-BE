package gymmi.workspace.workout.domain;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workspace.domain.entity.Workspace;

public class WorkoutHistoryInWorkspaceValidator {

    public static void validate(Workspace workspace, WorkoutHistory workoutHistory) {
        if (!workoutHistory.isIn(workspace)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_OBJECTION);
        }
    }
}
