package gymmi.workspace.service.domain.vote;

import gymmi.entity.TimeEntity;
import gymmi.workspace.service.domain.objection.Objection;
import gymmi.workspace.service.domain.workspace.Worker;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"worker_id", "tackle_id"})})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Getter
public class Vote extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "worker_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Worker worker;

    @JoinColumn(name = "objection_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Objection objection;

    @Column(nullable = false)
    private Boolean isApproved;

    @Column(nullable = false)
    private Boolean automatic;

    public Vote(Worker worker, Objection objection, Boolean isApproved) {
        this(worker, objection, isApproved, false);
    }

    public Vote(Worker worker, Objection objection, Boolean isApproved, Boolean automatic) {
        this.worker = worker;
        this.objection = objection;
        this.isApproved = isApproved;
        this.automatic = automatic;
    }

    public static Vote autoVote(Worker worker, Objection objection) {
        return new Vote(worker, objection, true, true);
    }

    public boolean isVotedBy(Worker worker) {
        return this.worker.equals(worker);
    }

    public boolean isIn(Objection objection) {
        return this.objection.equals(objection);
    }

}


