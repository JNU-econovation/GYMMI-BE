package gymmi.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WorkingMissionInWorkspaceRequest {

    private Long id;
    private Integer count;

    public WorkingMissionInWorkspaceRequest(Long id, Integer count) {
        this.id = id;
        this.count = count;
    }
}
