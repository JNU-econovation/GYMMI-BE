package gymmi.response;

import lombok.Getter;

@Getter
public class MatchingWorkerResponse {

    private final Boolean isWorker;

    public MatchingWorkerResponse(Boolean isWorker) {
        this.isWorker = isWorker;
    }
}
