package gymmi.workspace.response;

import gymmi.workspace.domain.entity.WorkoutRecord;
import lombok.Getter;

@Getter
public class WorkoutRecordResponse {

    private final String Mission;
    private final Integer count;
    private final Integer totalScore;

    public WorkoutRecordResponse(WorkoutRecord workoutRecord) {
        this.Mission = workoutRecord.getMission().getName();
        this.count = workoutRecord.getCount();
        this.totalScore = workoutRecord.getSum();
    }

}
