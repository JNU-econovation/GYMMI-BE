package gymmi.workspace.domain;

import gymmi.workspace.domain.entity.Worker;
import lombok.Getter;

@Getter
public class LeftWorker {
    private final Worker worker;
    private final boolean isLastLeaver;

    public LeftWorker(Worker worker, boolean isLastLeaver) {
        this.worker = worker;
        this.isLastLeaver = isLastLeaver;
    }
}
