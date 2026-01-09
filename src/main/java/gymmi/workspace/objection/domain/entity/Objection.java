package gymmi.workspace.objection.domain.entity;

import gymmi.etc.domain.entity.TimeEntity;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Getter
public class Objection extends TimeEntity {

    public static final int PERIOD_HOUR = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "worker_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Worker subject;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_history_id", nullable = false, unique = true)
    private WorkoutHistory workoutHistory;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private boolean isInProgress;

    @Builder
    public Objection(Worker subject, WorkoutHistory workoutHistory, String reason) {
        this.subject = subject;
        this.workoutHistory = workoutHistory;
        this.reason = reason;
        this.isInProgress = true;
    }


    public void close() {
        this.isInProgress = false;
    }

    public boolean isIn(Workspace workspace) {
        return subject.isJoinedIn(workspace);
    }

    public LocalDateTime getDeadline() {
        return getCreatedAt().plusHours(PERIOD_HOUR);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(getDeadline());
    }

    public void applyAndClose() {
        workoutHistory.cancel();
        close();
    }

    public boolean hasVoteBy(Worker worker) {
        return false;
    }
}


