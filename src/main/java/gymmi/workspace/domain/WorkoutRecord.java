package gymmi.workspace.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class WorkoutRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "working_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Worked worked;

    @JoinColumn(name = "mission_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Mission mission;

    @Column(nullable = false)
    private int count;

    public WorkoutRecord(Mission mission, int count) {
        this.mission = mission;
        this.count = count;
    }

    void setWorked(Worked worked) {
        this.worked = worked;
    }

    public int getSum() {
        return mission.getScore() * count;
    }

    public int getCount() {
        return count;
    }

    public boolean hasMission(Mission mission) {
        return this.mission.equals(mission);
    }

}

