package gymmi.workspace.domain;

import static gymmi.exceptionhandler.message.ErrorCode.NO_WORKOUT_HISTORY_EXIST_IN_WORKSPACE;

import gymmi.entity.TimeEntity;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void canBeReadIn(Workspace workspace) {
        if (!worker.isJoinedIn(workspace)) {
            throw new NotHavePermissionException(NO_WORKOUT_HISTORY_EXIST_IN_WORKSPACE);
        }
    }

}
