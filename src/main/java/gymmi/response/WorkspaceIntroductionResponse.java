package gymmi.response;

import gymmi.entity.Workspace;
import lombok.Getter;

@Getter
public class WorkspaceIntroductionResponse {

    private final String password;
    private final String description;
    private final String tag;
    private final Boolean isCreator;

    public WorkspaceIntroductionResponse(Workspace workspace, boolean isCreator) {
        this.password = workspace.getPassword();
        this.description = workspace.getDescription();
        this.tag = workspace.getTag();
        this.isCreator = isCreator;
    }
}
