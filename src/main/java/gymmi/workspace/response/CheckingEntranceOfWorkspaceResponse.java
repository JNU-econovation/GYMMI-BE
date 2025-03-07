package gymmi.workspace.response;

import lombok.Getter;

@Getter
public class CheckingEntranceOfWorkspaceResponse {

    private final Boolean isWorker;
    private final Boolean isFull;

    public CheckingEntranceOfWorkspaceResponse(Boolean isWorker, Boolean isFull) {
        this.isWorker = isWorker;
        this.isFull = isFull;
    }
}
