package gymmi.workspace.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TackleRequest {

    @NotBlank
    private String reason;

    public TackleRequest(String reason) {
        this.reason = reason;
    }
}
