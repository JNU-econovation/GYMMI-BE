package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.NotHavePermissionException;
import gymmi.exceptionhandler.message.ErrorCode;

public class WorkspaceEditManager {

    private final Workspace workspace;
    private final Worker creator;


    public WorkspaceEditManager(Workspace workspace, Worker creator) {
        this.workspace = workspace;
        this.creator = validate(creator);
    }

    private Worker validate(Worker worker) {
        if (!workspace.isCreatedBy(worker)) {
            throw new NotHavePermissionException(ErrorCode.NOT_WORKSPACE_CREATOR);
        }
        return worker;
    }

    public void edit(String description, String tag) {
        workspace.editDescription(description);
        workspace.editTag(tag);
    }


}
