package gymmi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchingWorkspacePasswordRequest {

    @NotBlank
    private String password;

    public MatchingWorkspacePasswordRequest(String password) {
        this.password = password;
    }
}
