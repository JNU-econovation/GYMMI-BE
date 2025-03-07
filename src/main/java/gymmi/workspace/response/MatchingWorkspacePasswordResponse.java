package gymmi.workspace.response;

import lombok.Getter;

@Getter
public class MatchingWorkspacePasswordResponse {

    private final boolean sameness;

    public MatchingWorkspacePasswordResponse(boolean sameness) {
        this.sameness = sameness;
    }
}
