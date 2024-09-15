package gymmi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

