package gymmi.workspace.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor
public class MissionRequest {

    @NotBlank
    private String mission;

    @NotBlank
    private Integer score;

    public MissionRequest(String mission, Integer score) {
        this.mission = mission;
        this.score = score;
    }
}
