package gymmi.workspace.domain.entity;

import gymmi.entity.TimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class WorkoutHistory extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "worker_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Worker worker;

    @JoinColumn(name = "workout_confirmation_id", nullable = false, unique = true)
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private WorkoutConfirmation workoutConfirmation;

    @Column(nullable = false)
    private boolean isApproved;

    @Column(nullable = false)
    private Integer totalScore;

    public WorkoutHistory(Worker worker, WorkoutConfirmation workoutConfirmation) {
        this.worker = worker;
        this.isApproved = true;
        this.workoutConfirmation = workoutConfirmation;
        this.totalScore = 0;
    }

    public void addTotalScore(int score) {
        totalScore += score;
    }

    public void cancel() {
        this.isApproved = false;
        worker.cancelContributedScore(totalScore);
        this.totalScore = 0;
    }
}
