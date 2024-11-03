package gymmi.workspace.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WorkingMissionInWorkspaceRequest {

    @NotNull
    private Long id;

    @NotNull
    private Integer count;

    public WorkingMissionInWorkspaceRequest(Long id, Integer count) {
        this.id = id;
        this.count = count;
    }
}
