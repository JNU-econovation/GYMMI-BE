package gymmi.global.common.eventlistener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ObjectionOpenEvent {

    private final Long workspaceId;
    private final Long objectionId;

}
