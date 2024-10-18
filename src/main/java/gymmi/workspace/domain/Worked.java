package gymmi.workspace.domain;

import gymmi.entity.TimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Worked extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "worker_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Worker worker;

    @Column(nullable = false)
    private boolean isApproved;

    @Column(nullable = false)
    private Integer totalScore;

    @OneToMany(mappedBy = "worked", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<WorkoutRecord> workoutRecords = new ArrayList<>();

    public Worked(Worker worker, List<WorkoutRecord> workoutRecords) {
        this.worker = worker;
        this.workoutRecords = new ArrayList<>(workoutRecords);
        setRelations(workoutRecords);
        this.isApproved = true;
        this.totalScore = calculateSum();
    }

    private void setRelations(List<WorkoutRecord> workoutRecords) {
        workoutRecords.stream()
                .forEach(workoutRecord -> workoutRecord.setWorked(this));
    }

    public void apply() {
        // 변경감지 기능 사용하는 메서드(worker)... 더 좋은 대안 없을까??
        worker.addWorkingScore(totalScore);
    }

    public int getSum() {
        return workoutRecords.stream()
                .map(WorkoutRecord::getSum)
                .reduce(0, Integer::sum);
    }

    private int calculateSum() {
        return workoutRecords.stream()
                .map(WorkoutRecord::getSum)
                .reduce(0, Integer::sum);
    }

}
