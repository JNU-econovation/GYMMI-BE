package gymmi.workspace.domain.entity;

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
    private WorkoutHistory workoutHistory;

    @JoinColumn(name = "mission_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Mission mission;

    @Column(nullable = false)
    private int count;

    public WorkoutRecord(Mission mission, int count) {
        this.mission = mission;
        this.count = count;
    }

    void setWorkoutHistory(WorkoutHistory workoutHistory) {
        this.workoutHistory = workoutHistory;
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

