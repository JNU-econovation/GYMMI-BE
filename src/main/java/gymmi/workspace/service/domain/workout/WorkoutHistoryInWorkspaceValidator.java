package gymmi.workspace.service.domain.workout;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.service.domain.workspace.Workspace;

public class WorkoutHistoryInWorkspaceValidator {

    public static void validate(Workspace workspace, WorkoutHistory workoutHistory) {
        if (!workoutHistory.isIn(workspace)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_OBJECTION);
        }
    }
}
