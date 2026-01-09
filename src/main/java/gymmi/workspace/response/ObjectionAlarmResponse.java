package gymmi.workspace.response;

import gymmi.workspace.service.domain.objection.Objection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ObjectionAlarmResponse {

    private Long objectionId;
    private Long workoutConfirmationId;
    private String targetWorkerNickname;
    private Boolean voteCompletion;
    private LocalDateTime createdAt;

    public ObjectionAlarmResponse(Objection objection, String targetWorkerNickname, Boolean voteCompletion) {
        this.objectionId = objection.getId();
        this.workoutConfirmationId = objection.getWorkoutHistory().getWorkoutConfirmation().getId();
        this.targetWorkerNickname = targetWorkerNickname;
        this.voteCompletion = voteCompletion;
        this.createdAt = objection.getCreatedAt();
    }

}
