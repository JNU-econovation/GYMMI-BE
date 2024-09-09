package gymmi.entity;

import lombok.Getter;

@Getter
public class WorkerJoinedEvent {

    private final Worker worker;
    private final Task task;

    public WorkerJoinedEvent(Worker worker, Task task) {
        this.worker = worker;
        this.task = task;
    }

}
