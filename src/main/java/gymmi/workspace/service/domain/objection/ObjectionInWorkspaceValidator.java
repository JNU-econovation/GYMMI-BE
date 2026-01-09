package gymmi.workspace.service.domain.objection;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.service.domain.workspace.Workspace;

public class ObjectionInWorkspaceValidator {

    public static void validate(Workspace workspace, Objection objection) {
        if (!objection.isIn(workspace)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_OBJECTION);
        }
    }
}
