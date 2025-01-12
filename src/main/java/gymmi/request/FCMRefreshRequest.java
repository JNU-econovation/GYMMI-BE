package gymmi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMRefreshRequest {

    @NotBlank
    private String token;

    public FCMRefreshRequest(String token) {
        this.token = token;
    }
}
