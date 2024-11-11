package gymmi.workspace.domain.entity;

import gymmi.entity.TimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @JoinColumn(name = "tackle_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Tackle tackle;

    @Column(nullable = false)
    private Boolean isAgree;

    public Vote(Worker worker, Tackle tackle, Boolean isAgree) {
        this.worker = worker;
        this.tackle = tackle;
        this.isAgree = isAgree;
        setRelations(tackle);
    }

    private void setRelations(Tackle tackle) {
        tackle.add(this);
    }

}


