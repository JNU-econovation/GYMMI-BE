package gymmi.workspace.response;

import gymmi.workspace.domain.entity.WorkoutHistory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkoutConfirmationResponse {

    private final Long workoutConfirmationId;
    private final String nickname;
    private final String profileImageUrl;
    private final String workoutConfirmationImageUrl;
    private final LocalDateTime createdAt;

    public WorkoutConfirmationResponse(WorkoutHistory workoutHistory, String workoutConfirmationImageUrl) {
        this.workoutConfirmationId = workoutHistory.getWorkoutConfirmation().getId();
        this.nickname = workoutHistory.getWorker().getUser().getNickname();
        this.profileImageUrl = workoutHistory.getWorker().getUser().getProfileImageName();
        this.workoutConfirmationImageUrl = workoutConfirmationImageUrl;
        this.createdAt = workoutHistory.getCreatedAt();
    }


}
