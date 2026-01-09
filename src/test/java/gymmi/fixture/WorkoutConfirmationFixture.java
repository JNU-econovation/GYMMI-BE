package gymmi.fixture;

import gymmi.workspace.workout.domain.entity.WorkoutConfirmation;
import org.springframework.test.util.ReflectionTestUtils;

public abstract class WorkoutConfirmationFixture {

    public static WorkoutConfirmationBuilder builder() {
        return new WorkoutConfirmationBuilder();
    }

    public static WorkoutConfirmation defaultWorkoutConfirmation() {
        return builder().build();
    }

    public static class WorkoutConfirmationBuilder {
        private Long id = null;
        private String filename = "/default_workout_image.png"; // 기본 파일명
        private String comment = "오늘 운동 완료!"; // 기본 코멘트

        private WorkoutConfirmationBuilder() {
        }

        public WorkoutConfirmationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public WorkoutConfirmationBuilder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public WorkoutConfirmationBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public WorkoutConfirmation build() {
            WorkoutConfirmation confirmation = new WorkoutConfirmation(filename, comment);

            if (id != null) {
                ReflectionTestUtils.setField(confirmation, "id", id);
            }

            return confirmation;
        }
    }
}
