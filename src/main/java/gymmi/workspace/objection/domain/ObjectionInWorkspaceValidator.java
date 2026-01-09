package gymmi.workspace.objection.domain;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.workspace.domain.entity.Workspace;

public class ObjectionInWorkspaceValidator {

    public static void validate(Workspace workspace, Objection objection) {
        if (!objection.isIn(workspace)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_OBJECTION);
        }
    }
}
