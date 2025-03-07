package gymmi.eventlistener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ObjectionOpenEvent {

    private final Long workspaceId;
    private final Long objectionId;

}
