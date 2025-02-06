package gymmi.workspace.response;

import gymmi.workspace.domain.entity.Worker;
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
