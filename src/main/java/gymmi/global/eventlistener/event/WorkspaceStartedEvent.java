package gymmi.global.eventlistener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WorkspaceStartedEvent {

    private final Long workspaceId;

}
