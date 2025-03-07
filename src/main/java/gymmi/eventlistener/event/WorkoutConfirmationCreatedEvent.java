package gymmi.eventlistener.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WorkoutConfirmationCreatedEvent {

    private final Long workspaceId;
    private final Long userId;

}
