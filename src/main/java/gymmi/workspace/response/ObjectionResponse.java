package gymmi.workspace.response;

import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.service.ObjectionWithVotes;
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
    private Integer headCount;
    private Boolean confirmationCompletion;

    @Builder
    public ObjectionResponse(
            LocalDateTime deadline, Boolean inInProgress, String reason,
            Integer voteParticipationCount, Boolean voteCompletion,
            Integer approvalCount, Integer rejectionCount, Boolean confirmationCompletion, Integer headCount
    ) {
        this.deadline = deadline;
        this.inInProgress = inInProgress;
        this.reason = reason;
        this.voteParticipationCount = voteParticipationCount;
        this.voteCompletion = voteCompletion;
        this.approvalCount = approvalCount;
        this.rejectionCount = rejectionCount;
        this.confirmationCompletion = confirmationCompletion;
        this.headCount = headCount;
    }

    public static ObjectionResponse closedObjection(ObjectionWithVotes objectionWithVotes, boolean voteCompletion, boolean confirmationCompletion, Integer headCount) {
        Objection objection = objectionWithVotes.getObjection();
        return ObjectionResponse.builder()
                .deadline(objection.getDeadline())
                .inInProgress(false)
                .reason(objection.getReason())
                .voteCompletion(voteCompletion)
                .voteParticipationCount(objectionWithVotes.getVoteCount())
                .approvalCount(objectionWithVotes.getApprovalCount())
                .rejectionCount(objectionWithVotes.getRejectionCount())
                .confirmationCompletion(confirmationCompletion)
                .headCount(headCount)
                .build();
    }

    public static ObjectionResponse objectionInProgressWithVoteInCompletion(ObjectionWithVotes objectionWithVotes, Integer headCount) {
        Objection objection = objectionWithVotes.getObjection();
        return ObjectionResponse.builder()
                .deadline(objection.getDeadline())
                .inInProgress(true)
                .reason(objection.getReason())
                .voteCompletion(false)
                .voteParticipationCount(objectionWithVotes.getVoteCount())
                .approvalCount(null)
                .rejectionCount(null)
                .confirmationCompletion(null)
                .headCount(headCount)
                .build();
    }

    public static ObjectionResponse objectionInProgressWithVoteCompletion(ObjectionWithVotes objectionWithVotes, Integer headCount) {
        Objection objection = objectionWithVotes.getObjection();
        return ObjectionResponse.builder()
                .deadline(objection.getDeadline())
                .inInProgress(true)
                .reason(objection.getReason())
                .voteCompletion(true)
                .voteParticipationCount(objectionWithVotes.getVoteCount())
                .approvalCount(objectionWithVotes.getApprovalCount())
                .rejectionCount(objectionWithVotes.getRejectionCount())
                .confirmationCompletion(null)
                .headCount(headCount)
                .build();
    }

}
