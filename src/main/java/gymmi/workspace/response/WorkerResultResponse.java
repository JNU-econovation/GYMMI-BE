package gymmi.workspace.response;

import gymmi.workspace.service.domain.workspace.Worker;
import lombok.Builder;
import lombok.Getter;

@Getter
public class WorkerResultResponse {

    private final String name;
    private final Integer contributeScore;
    private final Integer rank;

    @Builder
    public WorkerResultResponse(Worker worker, Integer rank) {
        this.name = worker.getNickname();
        this.contributeScore = worker.getContributedScore();
        this.rank = rank;
    }

}
