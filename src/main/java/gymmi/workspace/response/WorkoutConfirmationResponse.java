package gymmi.workspace.response;

import lombok.Getter;

import java.util.List;

@Getter
public class WorkoutConfirmationResponse {

    private final List<WorkoutConfirmationOrObjectionResponse> data;
    private final Integer voteIncompletionCount;

    public WorkoutConfirmationResponse(List<WorkoutConfirmationOrObjectionResponse> data, Integer voteIncompletionCount) {
        this.data = data;
        this.voteIncompletionCount = voteIncompletionCount;
    }
}
