package gymmi.workspace.domain;

import gymmi.workspace.domain.entity.WorkoutHistory;

import java.util.Collections;
import java.util.List;

public class WorkoutMetric {

    private final List<WorkoutHistory> workoutHistories;

    public WorkoutMetric(List<WorkoutHistory> workoutHistories) {
        this.workoutHistories = Collections.unmodifiableList(workoutHistories);
    }

    public int getWorkoutCount() {
        return (int) workoutHistories.stream()
                .filter(WorkoutHistory::isApproved)
                .count();
    }

    public int getBestWorkoutScore() {
        return workoutHistories.stream()
                .filter(WorkoutHistory::isApproved)
                .mapToInt(WorkoutHistory::getTotalScore)
                .max().orElse(0);
    }

    public int getSum() {
        return workoutHistories.stream()
                .filter(WorkoutHistory::isApproved)
                .map(WorkoutHistory::getTotalScore)
                .reduce(0, Integer::sum);
    }

    public int getScoreGapFrom(int firstPlaceScore) {
        int scoreGap = firstPlaceScore - getSum();
        if (scoreGap > 0) {
            return scoreGap;
        }
        return 0;
    }

}
