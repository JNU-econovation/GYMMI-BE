package gymmi.workspace.objection.controller.response;

import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.vote.domain.VoteResult;
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

    public static ObjectionResponse closedObjection(Objection objection, VoteResult voteResult, boolean voteCompletion, boolean confirmationCompletion, Integer headCount) {
        return ObjectionResponse.builder()
                .deadline(objection.getDeadline())
                .inInProgress(false)
                .reason(objection.getReason())
                .voteCompletion(voteCompletion)
                .voteParticipationCount(voteResult.voteCount())
                .approvalCount(voteResult.approvalCount())
                .rejectionCount(voteResult.rejectionCount())
                .confirmationCompletion(confirmationCompletion)
                .headCount(headCount)
                .build();
    }

    public static ObjectionResponse objectionInProgressWithVoteInCompletion(Objection objection, VoteResult voteResult, Integer headCount) {
        return ObjectionResponse.builder()
                .deadline(objection.getDeadline())
                .inInProgress(true)
                .reason(objection.getReason())
                .voteCompletion(false)
                .voteParticipationCount(voteResult.voteCount())
                .approvalCount(null)
                .rejectionCount(null)
                .confirmationCompletion(null)
                .headCount(headCount)
                .build();
    }

    public static ObjectionResponse objectionInProgressWithVoteCompletion(Objection objection, VoteResult voteResult, Integer headCount) {
        return ObjectionResponse.builder()
                .deadline(objection.getDeadline())
                .inInProgress(true)
                .reason(objection.getReason())
                .voteCompletion(true)
                .voteParticipationCount(voteResult.voteCount())
                .approvalCount(voteResult.approvalCount())
                .rejectionCount(voteResult.rejectionCount())
                .confirmationCompletion(null)
                .headCount(headCount)
                .build();
    }

}
