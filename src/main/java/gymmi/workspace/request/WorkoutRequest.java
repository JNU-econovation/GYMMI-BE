package gymmi.workspace.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WorkoutRequest {

    @NotNull
    private String imageUrl;

    private String comment;

    @NotNull
    private Boolean willLink;

    @NotNull
    private List<WorkingMissionInWorkspaceRequest> missions;

    public WorkoutRequest(
            String imageUrl,
            String comment,
            Boolean willLink,
            List<WorkingMissionInWorkspaceRequest> missions
    ) {
        this.imageUrl = imageUrl;
        this.comment = comment;
        this.willLink = willLink;
        this.missions = missions;
    }
}
