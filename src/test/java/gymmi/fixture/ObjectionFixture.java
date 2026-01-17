package gymmi.fixture;

import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workspace.domain.entity.Worker;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public abstract class ObjectionFixture {

    public static ObjectionBuilder builder(Worker subject, WorkoutHistory workoutHistory) {
        return new ObjectionBuilder(subject, workoutHistory);
    }

    public static class ObjectionBuilder {
        private final Worker subject;
        private final WorkoutHistory workoutHistory;

        private Long id = 0L;
        private String reason = "운동 인증이 정당하지 않습니다.";
        private boolean isInProgress = true;
        private LocalDateTime createdAt = null;

        private ObjectionBuilder(Worker subject, WorkoutHistory workoutHistory) {
            this.subject = subject;
            this.workoutHistory = workoutHistory;
        }

        public ObjectionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ObjectionBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public ObjectionBuilder isInProgress(boolean isInProgress) {
            this.isInProgress = isInProgress;
            return this;
        }

        /**
         * TimeEntity의 createdAt을 수동으로 설정해야 할 때 사용 (예: 만료 테스트)
         */
        public ObjectionBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Objection build() {
            Objection objection = Objection.builder()
                .subject(subject)
                .workoutHistory(workoutHistory)
                .reason(reason)
                .build();

            // 필드 수동 설정 (ID 및 진행 상태)
            if (id != null) {
                ReflectionTestUtils.setField(objection, "id", id);
            }
            if (!isInProgress) {
                ReflectionTestUtils.setField(objection, "isInProgress", false);
            }
            if (createdAt != null) {
                ReflectionTestUtils.setField(objection, "createdAt", createdAt);
            }

            return objection;
        }
    }
}
