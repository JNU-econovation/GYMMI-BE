package gymmi.entity;

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
