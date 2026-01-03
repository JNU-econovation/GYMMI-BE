package gymmi.fixture;

import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.WorkoutConfirmation;
import gymmi.workspace.domain.entity.WorkoutHistory;
import org.springframework.test.util.ReflectionTestUtils;

public abstract  class WorkoutHistoryFixture {

    public static WorkoutHistoryBuilder builder(Worker worker, WorkoutConfirmation confirmation) {
        return new WorkoutHistoryBuilder(worker, confirmation);
    }

    public static class WorkoutHistoryBuilder {
        private final Worker worker;
        private final WorkoutConfirmation workoutConfirmation;

        private Long id = null;
        private boolean isApproved = true;
        private Integer totalScore = 0;

        private WorkoutHistoryBuilder(Worker worker, WorkoutConfirmation confirmation) {
            this.worker = worker;
            this.workoutConfirmation = confirmation;
        }

        public WorkoutHistoryBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public WorkoutHistoryBuilder isApproved(boolean isApproved) {
            this.isApproved = isApproved;
            return this;
        }

        public WorkoutHistoryBuilder totalScore(Integer totalScore) {
            this.totalScore = totalScore;
            return this;
        }

        public WorkoutHistory build() {
            WorkoutHistory workoutHistory = new WorkoutHistory(worker, workoutConfirmation);

            if (id != null) {
                ReflectionTestUtils.setField(workoutHistory, "id", id);
            }

            ReflectionTestUtils.setField(workoutHistory, "isApproved", isApproved);
            ReflectionTestUtils.setField(workoutHistory, "totalScore", totalScore);

            return workoutHistory;
        }
    }
}
