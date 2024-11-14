package gymmi.workspace.response;

import gymmi.workspace.domain.entity.Objection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ObjectionResponse {

    private LocalDateTime deadline;
    private Boolean inInProgress;
    private String reason;
    private Integer voteParticipationCount;
    private Boolean voteCompletion;
    private Integer approvalCount;
    private Integer rejectionCount;
    private Boolean confirmationCompletion;

    @Builder
    public ObjectionResponse(
            LocalDateTime deadline, Boolean inInProgress, String reason,
            Integer voteParticipationCount, Boolean voteCompletion,
            Integer approvalCount, Integer rejectionCount, Boolean confirmationCompletion
    ) {
        this.deadline = deadline;
        this.inInProgress = inInProgress;
        this.reason = reason;
        this.voteParticipationCount = voteParticipationCount;
        this.voteCompletion = voteCompletion;
        this.approvalCount = approvalCount;
        this.rejectionCount = rejectionCount;
        this.confirmationCompletion = confirmationCompletion;
    }

    public static ObjectionResponse closedObjection(Objection objection, boolean voteCompletion, boolean confirmationCompletion) {
        return ObjectionResponse.builder()
                .deadline(objection.getDeadline())
                .inInProgress(false)
                .reason(objection.getReason())
                .voteCompletion(voteCompletion)
                .voteParticipationCount(objection.getVoteCount())
                .approvalCount(objection.getApprovalCount())
                .rejectionCount(objection.getRejectionCount())
                .confirmationCompletion(confirmationCompletion)
                .build();
    }

    public static ObjectionResponse objectionInProgressWithVoteInCompletion(Objection objection) {
        return ObjectionResponse.builder()
                .deadline(objection.getDeadline())
                .inInProgress(true)
                .reason(objection.getReason())
                .voteCompletion(false)
                .voteParticipationCount(objection.getVoteCount())
                .approvalCount(null)
                .rejectionCount(null)
                .confirmationCompletion(null)
                .build();
    }

    public static ObjectionResponse objectionInProgressWithVoteCompletion(Objection objection) {
        return ObjectionResponse.builder()
                .deadline(objection.getDeadline())
                .inInProgress(true)
                .reason(objection.getReason())
                .voteCompletion(true)
                .voteParticipationCount(objection.getVoteCount())
                .approvalCount(objection.getApprovalCount())
                .rejectionCount(objection.getRejectionCount())
                .confirmationCompletion(null)
                .build();
    }

}
