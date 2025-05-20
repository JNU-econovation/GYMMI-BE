package gymmi.workspace.repository;

import gymmi.workspace.domain.entity.WorkoutRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkoutRecordRepository extends JpaRepository<WorkoutRecord, Long> {

    @Query("select w from WorkoutRecord w where w.workoutHistory.id = :workoutHistoryId")
    List<WorkoutRecord> getAllByWorkoutHistoryId(Long workoutHistoryId);

}
