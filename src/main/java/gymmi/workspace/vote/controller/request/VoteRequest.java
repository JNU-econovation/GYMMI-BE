package gymmi.workspace.vote.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteRequest {

    @NotNull
    private Boolean willApprove;

    public VoteRequest(Boolean willApprove) {
        this.willApprove = willApprove;
    }
}
