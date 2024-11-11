package gymmi.workspace.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteRequest {

    @NotBlank
    private Boolean isAgree;

    public VoteRequest(Boolean isAgree) {
        this.isAgree = isAgree;
    }
}
