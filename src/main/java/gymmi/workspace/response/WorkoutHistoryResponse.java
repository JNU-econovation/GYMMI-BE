package gymmi.workspace.response;

import gymmi.workspace.domain.entity.WorkoutHistory;
import gymmi.workspace.service.WorkoutProcessor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkoutHistoryResponse {

    private Long id;
    private Boolean isApproved;
    private LocalDateTime createdAt;
    private Integer sumOfScore;

    public WorkoutHistoryResponse(WorkoutHistory workoutHistory, WorkoutProcessor workoutProcessor) {
        this.id = workoutHistory.getId();
        this.isApproved = workoutHistory.isApproved();
        this.createdAt = workoutHistory.getCreatedAt();
        this.sumOfScore = workoutHistory.getSum();
    }
}
