package gymmi.workspace.domain.entity;

import gymmi.entity.TimeEntity;
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

    public Vote(Worker worker, Objection objection, Boolean isApproved) {
        this.worker = worker;
        this.objection = objection;
        this.isApproved = isApproved;
        setRelations(objection);
    }

    private void setRelations(Objection objection) {
        objection.add(this);
    }

}


