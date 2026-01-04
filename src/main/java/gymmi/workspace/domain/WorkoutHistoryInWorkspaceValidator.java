package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.WorkoutHistory;
import gymmi.workspace.domain.entity.Workspace;

public class WorkoutHistoryInWorkspaceValidator {

    public static void validate(Workspace workspace, WorkoutHistory workoutHistory) {
        if (!workoutHistory.isIn(workspace)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_OBJECTION);
        }
    }
}
