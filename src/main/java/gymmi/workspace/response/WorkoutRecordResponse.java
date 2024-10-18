package gymmi.workspace.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkoutRecordResponse {

    private final Long id;
    private final boolean isApproved;
    private final LocalDateTime createdAt;
    private final Integer totalContributedScore;

    public WorkoutRecordResponse(Long id, boolean isApproved, LocalDateTime createdAt, Integer totalContributedScore) {
        this.id = id;
        this.isApproved = isApproved;
        this.createdAt = createdAt;
        this.totalContributedScore = totalContributedScore;
    }

}
