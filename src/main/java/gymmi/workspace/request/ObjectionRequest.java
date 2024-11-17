package gymmi.workspace.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ObjectionRequest {

    @NotBlank
    private String reason;

    public ObjectionRequest(String reason) {
        this.reason = reason;
    }
}
