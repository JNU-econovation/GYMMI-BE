package gymmi.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreatingWorkspaceRequest {

    @NotBlank
    @Length(max = 9, message = "워크스페이스 이름은 9자까지 가능합니다.")
    private String name;

    @Range(min = 2, max = 9, message = "워크스페이스 인원수는 2~9명까지 가능합니다.")
    private Integer headCount;

    @Range(min = 100, max = 1000, message = "목표점수는 100점에서 1000점까지 가능합니다.")
    private Integer goalScore;

    private String description;

    @Length(max = 10, message = "태그의 글자수는 10자까지 가능합니다.")
    private String tag;

    @Size(min = 1, max = 15, message = "미션 개수는 1~15개까지 가능합니다.")
    private List<MissionRequest> missionBoard;

    @NotBlank(message = "테스크를 입력해주세요.")
    private String task;

    @Builder
    public CreatingWorkspaceRequest(
            String name, Integer headCount,
            Integer goalScore, String description, String tag,
            List<MissionRequest> missionBoard, String task
    ) {
        this.name = name;
        this.headCount = headCount;
        this.goalScore = goalScore;
        this.description = description;
        this.tag = tag;
        this.missionBoard = missionBoard;
        this.task = task;
    }
}
