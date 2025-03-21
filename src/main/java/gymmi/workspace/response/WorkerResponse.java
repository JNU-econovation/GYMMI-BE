package gymmi.workspace.response;

import gymmi.workspace.domain.entity.Worker;
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
    private final String profileImage;

    @Builder
    public WorkerResponse(Worker worker, Integer rank, boolean isCreator, boolean isMyself) {
        this.id = worker.getUser().getId();
        this.name = worker.getNickname();
        this.contributeScore = worker.getContributedScore();
        this.rank = rank;
        this.isCreator = isCreator;
        this.isMyself = isMyself;
        this.profileImage = worker.getUser().getProfileImageName();
    }

}
