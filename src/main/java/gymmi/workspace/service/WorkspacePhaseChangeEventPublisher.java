package gymmi.workspace.service;

import gymmi.eventlistener.event.WorkspacePhaseChangedEvent;
import gymmi.workspace.domain.WorkspacePhase;
import gymmi.workspace.domain.entity.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkspacePhaseChangeEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(Workspace workspace, int contributedScore) {
        if (workspace.isCompleted()) {
            applicationEventPublisher.publishEvent(new WorkspacePhaseChangedEvent(workspace.getId(), WorkspacePhase.P_100));
            return;
        }
        WorkspacePhase beforeWorkspacePhase = WorkspacePhase.from(workspace.getGoalScore(), workspace.getCurrentScore() - contributedScore);
        WorkspacePhase afterWorkspacePhase = WorkspacePhase.from(workspace.getGoalScore(), workspace.getCurrentScore());
        if (beforeWorkspacePhase != afterWorkspacePhase) {
            applicationEventPublisher.publishEvent(new WorkspacePhaseChangedEvent(workspace.getId(), afterWorkspacePhase));
        }
    }

}
