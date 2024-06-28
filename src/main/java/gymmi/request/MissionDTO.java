package gymmi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor
public class MissionDTO {

    @NotBlank
    @Length(max = 20)
    private String mission;

    @NotBlank
    @Range(min = 1, max = 10)
    private Integer score;

    public MissionDTO(String mission, Integer score) {
        this.mission = mission;
        this.score = score;
    }
}
