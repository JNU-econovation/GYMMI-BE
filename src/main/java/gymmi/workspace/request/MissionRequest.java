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
    @Length(max = 20, message = "미션 글자수는 20자까지 가능합니다.")
    private String mission;

    @NotBlank
    @Range(min = 1, max = 10, message = "미션 점수는 1~10점까지 가능합니다.")
    private Integer score;

    public MissionRequest(String mission, Integer score) {
        this.mission = mission;
        this.score = score;
    }
}
