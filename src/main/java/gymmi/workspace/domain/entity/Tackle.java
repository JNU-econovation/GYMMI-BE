package gymmi.workspace.domain.entity;

import gymmi.entity.TimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Tackle extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "worker_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Worker worker;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_proof_id", nullable = false)
    private WorkoutProof workoutProof;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private boolean isOpen;

    @Builder
    public Tackle(Worker worker, WorkoutProof workoutProof, String reason) {
        this.worker = worker;
        this.workoutProof = workoutProof;
        this.reason = reason;
        this.isOpen = true;
    }
}


