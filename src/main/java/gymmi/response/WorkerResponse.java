package gymmi.response;

import gymmi.entity.Worker;
import lombok.Builder;
import lombok.Getter;

@Getter
public class WorkerResponse {

    private final Long id;
    private final String name;
    private final Integer contributeScore;
    private final Integer rank;
    private final Boolean isCreator;
    private final Boolean isMyself;

    @Builder
    public WorkerResponse(Worker worker, Integer rank, boolean isCreator, boolean isMyself) {
        this.id = worker.getId();
        this.name = worker.getNickname();
        this.contributeScore = worker.getContributedScore();
        this.rank = rank;
        this.isCreator = isCreator;
        this.isMyself = isMyself;
    }

}
