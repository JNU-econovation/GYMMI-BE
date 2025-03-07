package gymmi.workspace.response;

import gymmi.workspace.domain.entity.Workspace;
import lombok.Getter;

@Getter
public class WorkspaceIntroductionResponse {

    private final String password;
    private final String description;
    private final String tag;
    private final String task;
    private final Boolean isCreator;
    private final Boolean isPreparing;

    public WorkspaceIntroductionResponse(Workspace workspace, boolean isCreator) {
        this.password = workspace.getPassword();
        this.description = workspace.getDescription();
        this.isPreparing = workspace.isPreparing();
        this.tag = workspace.getTag();
        this.task = workspace.getTask();
        this.isCreator = isCreator;
    }
}
