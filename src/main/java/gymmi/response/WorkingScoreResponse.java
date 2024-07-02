package gymmi.response;


import lombok.Getter;

@Getter
public class WorkingScoreResponse {

    private final int workingScore;

    public WorkingScoreResponse(int workingScore) {
        this.workingScore = workingScore;
    }
}
