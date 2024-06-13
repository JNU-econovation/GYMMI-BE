package gymmi.response;

import lombok.Getter;

@Getter
public class WorkspacePasswordResponse {

    private final String password;

    public WorkspacePasswordResponse(String password) {
        this.password = password;
    }
}
