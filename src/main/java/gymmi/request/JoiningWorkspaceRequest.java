package gymmi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.crypto.Mac;

@Getter
@NoArgsConstructor
public class JoiningWorkspaceRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "테스크를 입력해주세요.")
    private String task;

    public JoiningWorkspaceRequest(String password, String task) {
        this.password = password;
        this.task = task;
    }
}
