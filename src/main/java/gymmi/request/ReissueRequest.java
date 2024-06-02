package gymmi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueRequest {

    @NotBlank
    private String refreshToken;

    public ReissueRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
