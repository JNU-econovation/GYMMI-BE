package gymmi.workspace.workout.controller.response;

import gymmi.workspace.workout.domain.entity.WorkoutRecord;
import lombok.Getter;

@Getter
public class WorkoutRecordResponse {

    private final Long id;
    private final String Mission;
    private final Integer count;
    private final Integer totalScore;

    public WorkoutRecordResponse(WorkoutRecord workoutRecord) {
        this.id = workoutRecord.getId();
        this.Mission = workoutRecord.getMission().getName();
        this.count = workoutRecord.getCount();
        this.totalScore = workoutRecord.getSum();
    }

}
