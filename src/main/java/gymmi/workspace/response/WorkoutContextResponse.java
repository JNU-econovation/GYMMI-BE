package gymmi.workspace.response;

import gymmi.workspace.domain.entity.WorkoutHistory;
import gymmi.workspace.domain.WorkoutMetric;
import lombok.Getter;

import java.util.List;

@Getter
public class WorkoutContextResponse {

    private Integer totalContributedScore;
    private Integer bestDailyScore;
    private Integer totalWorkoutCount;
    private Integer gabScoreFromFirst;
    private List<WorkoutHistoryResponse> workoutHistories;

    public WorkoutContextResponse(WorkoutMetric workoutMetric, int gabScoreFromFirst, List<WorkoutHistory> workoutHistories) {
        this.totalContributedScore = workoutMetric.getSum();
        this.bestDailyScore = workoutMetric.getBestWorkoutScore();
        this.totalWorkoutCount = workoutMetric.getWorkoutCount();
        this.gabScoreFromFirst = gabScoreFromFirst;
        this.workoutHistories = getWorkoutHistories(workoutHistories);
    }

    private List<WorkoutHistoryResponse> getWorkoutHistories(List<WorkoutHistory> workoutHistories) {
        return workoutHistories.stream()
                .map(WorkoutHistoryResponse::new)
                .toList();
    }

}
