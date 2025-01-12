package gymmi.eventlistener;

import gymmi.eventlistener.event.WorkspaceStartedEvent;
import gymmi.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AlarmEventListener {

    private final AlarmService alarmService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void push(WorkspaceStartedEvent workspaceStartedEvent) {
        alarmService.notifyWorkspaceStart(workspaceStartedEvent.getWorkspaceId());
    }

}
