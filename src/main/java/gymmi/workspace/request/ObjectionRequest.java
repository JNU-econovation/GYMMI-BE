package gymmi.workspace.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class ObjectionRequest {

    @NotBlank
    @Length(min = 10, max = 150)
    private String reason;

    public ObjectionRequest(String reason) {
        this.reason = reason;
    }
}
