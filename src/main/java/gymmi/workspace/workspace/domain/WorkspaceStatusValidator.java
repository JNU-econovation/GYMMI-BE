package gymmi.workspace.workspace.domain;

import gymmi.global.exceptionhandler.exception.InvalidStateException;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.workspace.workspace.domain.entity.Workspace;

public class WorkspaceStatusValidator {

    public static void validateWorkspaceIsInProgress(Workspace workspace) {
        if (!workspace.isInProgress()) {
            throw new InvalidStateException(ErrorCode.INACTIVE_WORKSPACE);
        }
    }
}
