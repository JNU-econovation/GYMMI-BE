package gymmi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoiningWorkspaceRequest {

    @NotBlank
    private String password;

    @NotBlank
    private String task;

    public JoiningWorkspaceRequest(String password, String task) {
        this.password = password;
        this.task = task;
    }
}
