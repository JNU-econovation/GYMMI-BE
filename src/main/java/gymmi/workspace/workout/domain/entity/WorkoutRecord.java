package gymmi.workspace.workout.domain.entity;

import gymmi.workspace.mission.domain.entity.Mission;
import jakarta.persistence.*;
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

    @JoinColumn(name = "workout_history_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private WorkoutHistory workoutHistory;

    @JoinColumn(name = "mission_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Mission mission;

    @Column(nullable = false)
    private int count;

    public WorkoutRecord(WorkoutHistory workoutHistory, Mission mission, int count) {
        this.workoutHistory = workoutHistory;
        this.mission = mission;
        this.count = count;
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

