package gymmi.workspace.response;

import lombok.Getter;

@Getter
public class CheckingCreationOfWorkspaceResponse {

    private final Boolean canCreate;

    public CheckingCreationOfWorkspaceResponse(Boolean canCreate) {
        this.canCreate = canCreate;
    }
}
