package gymmi.workspace.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String name;

    @NotNull
    private Integer headCount;

    @NotNull
    private Integer goalScore;

    private String description;

    private String tag;

    private List<MissionRequest> missionBoard;

    @NotBlank
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
