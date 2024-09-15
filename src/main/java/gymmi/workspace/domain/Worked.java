package gymmi.workspace.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Worked {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "worker_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Worker worker;

    @OneToMany(mappedBy = "worked", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<WorkoutRecord> workoutRecords = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public Worked(Worker worker, List<WorkoutRecord> workoutRecords) {
        this.worker = worker;
        this.workoutRecords = new ArrayList<>(workoutRecords);
        setRelations(workoutRecords);
    }

    private void setRelations(List<WorkoutRecord> workoutRecords) {
        workoutRecords.stream()
                .forEach(workoutRecord -> workoutRecord.setWorked(this));
    }

    public void apply() {
        worker.addWorkingScore(getSum());
    }

    public int getSum() {
        return workoutRecords.stream()
                .map(WorkoutRecord::getSum)
                .reduce(0, Integer::sum);
    }
}
