package gymmi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResignRequest {

    @NotBlank
    private String password;

    public ResignRequest(String password) {
        this.password = password;
    }
}
