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
    @Length(max = 9)
    private String name;

    @Range(min = 2, max = 9)
    private Integer headCount;

    @Range(min = 100, max = 1000)
    private Integer goalScore;

    private String description;

    @Length(max = 10)
    private String tag;

    @Size(min = 1, max = 15)
    private List<MissionDTO> missionBoard;

    @NotBlank
    private String task;

    @Builder
    public CreatingWorkspaceRequest(
            String name, Integer headCount,
            Integer goalScore, String description, String tag,
            List<MissionDTO> missionBoard, String task
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
