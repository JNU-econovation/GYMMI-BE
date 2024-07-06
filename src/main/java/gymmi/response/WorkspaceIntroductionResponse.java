package gymmi.response;

import gymmi.entity.Workspace;
import lombok.Getter;

@Getter
public class WorkspaceIntroductionResponse {

    private final String password;
    private final String description;
    private final String tag;

    public WorkspaceIntroductionResponse(Workspace workspace) {
        this.password = workspace.getPassword();
        this.description = workspace.getDescription();
        this.tag = workspace.getTag();
    }
}
