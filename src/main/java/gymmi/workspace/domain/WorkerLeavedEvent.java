package gymmi.workspace.domain;

import gymmi.workspace.domain.entity.Worker;
import lombok.Getter;

@Getter
public class WorkerLeavedEvent {
    private final Worker worker;
    private final boolean isLastOne;

    public WorkerLeavedEvent(Worker worker, boolean isLastOne) {
        this.worker = worker;
        this.isLastOne = isLastOne;
    }
}
