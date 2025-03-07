package gymmi.workspace.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchingWorkspacePasswordRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public MatchingWorkspacePasswordRequest(String password) {
        this.password = password;
    }
}
