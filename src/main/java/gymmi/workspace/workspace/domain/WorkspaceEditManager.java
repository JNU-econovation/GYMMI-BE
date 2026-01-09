package gymmi.workspace.workspace.domain;

import gymmi.global.exceptionhandler.exception.NotHavePermissionException;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;

public class WorkspaceEditManager {

    private final Workspace workspace;
    private final Worker creator;


    public WorkspaceEditManager(Workspace workspace, Worker creator) {
        this.workspace = workspace;
        this.creator = validate(creator);
    }

    private Worker validate(Worker worker) {
        if (!worker.isCreator(workspace)) {
            throw new NotHavePermissionException(ErrorCode.NOT_WORKSPACE_CREATOR);
        }
        return worker;
    }

    public void edit(String description, String tag, String task) {
        workspace.editDescription(description);
        workspace.editTag(tag);
        workspace.editTask(task);
    }

}
