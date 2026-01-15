package gymmi.global.common.eventlistener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WorkspaceStartedEvent {

    private final Long workspaceId;

}
