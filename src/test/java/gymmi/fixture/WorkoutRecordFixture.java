package gymmi.fixture;

import gymmi.workspace.domain.entity.Mission;
import gymmi.workspace.domain.entity.WorkoutHistory;
import gymmi.workspace.domain.entity.WorkoutRecord;
import org.springframework.test.util.ReflectionTestUtils;

public abstract class WorkoutRecordFixture {

    public static WorkoutRecordBuilder builder(WorkoutHistory workoutHistory, Mission mission) {
        return new WorkoutRecordBuilder(workoutHistory, mission);
    }

    public static class WorkoutRecordBuilder {
        private final WorkoutHistory workoutHistory;
        private final Mission mission;

        private Long id = null;
        private int count = 0;

        private WorkoutRecordBuilder(WorkoutHistory workoutHistory, Mission mission) {
            this.workoutHistory = workoutHistory;
            this.mission = mission;
        }

        public WorkoutRecordBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public WorkoutRecordBuilder count(int count) {
            this.count = count;
            return this;
        }

        public WorkoutRecord build() {
            WorkoutRecord workoutRecord = new WorkoutRecord(
                workoutHistory, 
                mission, 
                count
            );

            if (id != null) {
                ReflectionTestUtils.setField(workoutRecord, "id", id);
            }

            return workoutRecord;
        }
    }
}
