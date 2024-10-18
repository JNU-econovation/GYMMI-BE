package gymmi.workspace.response;

import gymmi.workspace.domain.Worked;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkoutHistoryResponse {

    private Long id;
    private Boolean isApproved;
    private LocalDateTime createdAt;
    private Integer sumOfScore;

    public WorkoutHistoryResponse(Worked worked) {
        this.id = worked.getId();
        this.isApproved = worked.isApproved();
        this.createdAt = worked.getCreatedAt();
        this.sumOfScore = worked.getSum();
    }
}
