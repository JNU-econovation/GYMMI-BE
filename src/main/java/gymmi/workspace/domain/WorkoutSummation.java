package gymmi.workspace.domain;

import java.util.List;

public class WorkoutSummation {

    private final List<WorkoutRecord> workoutRecords;

    public WorkoutSummation(List<WorkoutRecord> workoutRecords) {
        this.workoutRecords = workoutRecords;
    }

    public int getSumOfMissionScoreContributed(Mission mission) {
        return workoutRecords.stream()
                .filter(wr -> wr.hasMission(mission))
                .map(WorkoutRecord::getSum)
                .reduce(0, Integer::sum);
    }

    public int getSumOfMissionCountWorked(Mission mission) {
        return workoutRecords.stream()
                .filter(wr -> wr.hasMission(mission))
                .map(WorkoutRecord::getCount)
                .reduce(0, Integer::sum);
    }
}
