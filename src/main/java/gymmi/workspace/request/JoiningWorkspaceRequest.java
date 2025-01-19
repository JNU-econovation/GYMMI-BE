package gymmi.workspace.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoiningWorkspaceRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public JoiningWorkspaceRequest(String password) {
        this.password = password;
    }
}
