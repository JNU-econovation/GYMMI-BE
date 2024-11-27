package gymmi.workspace.response;

import gymmi.workspace.domain.entity.Objection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ObjectionAlarmResponse {

    private Long objectionId;
    private String targetWorkerNickname;
    private Boolean voteCompletion;
    private LocalDateTime createdAt;

    public ObjectionAlarmResponse(Objection objection, String targetWorkerNickname, Boolean voteCompletion) {
        this.objectionId = objection.getId();
        this.targetWorkerNickname = targetWorkerNickname;
        this.voteCompletion = voteCompletion;
        this.createdAt = objection.getCreatedAt();
    }

}
