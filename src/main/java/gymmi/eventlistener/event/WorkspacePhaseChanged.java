package gymmi.eventlistener.event;

import gymmi.workspace.domain.WorkspacePhase;
import lombok.Getter;

@Getter
public class WorkspacePhaseChanged {

    private final Long workspaceId;
    private final WorkspacePhase workspacePhase;

    public WorkspacePhaseChanged(Long workspaceId, WorkspacePhase workspacePhase) {
        this.workspaceId = workspaceId;
        this.workspacePhase = workspacePhase;
    }
}
