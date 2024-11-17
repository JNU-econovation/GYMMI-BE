package gymmi.workspace.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteRequest {

    @NotBlank
    private Boolean willApprove;

    public VoteRequest(Boolean willApprove) {
        this.willApprove = willApprove;
    }
}
