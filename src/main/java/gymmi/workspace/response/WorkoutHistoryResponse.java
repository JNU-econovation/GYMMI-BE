package gymmi.workspace.response;

import gymmi.workspace.service.domain.workout.WorkoutHistory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkoutHistoryResponse {

    private Long id;
    private Boolean isApproved;
    private LocalDateTime createdAt;
    private Integer sumOfScore;

    public WorkoutHistoryResponse(WorkoutHistory workoutHistory) {
        this.id = workoutHistory.getId();
        this.isApproved = workoutHistory.isRejected();
        this.createdAt = workoutHistory.getCreatedAt();
        this.sumOfScore = workoutHistory.getTotalScore();
    }
}
