package gymmi.workspace.repository;

import gymmi.workspace.domain.entity.WorkoutRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkoutRecordRepository extends JpaRepository<WorkoutRecord, Long> {

    @Query("select w from WorkoutRecord w where w.workoutHistory.id = :workoutHistoryId")
    List<WorkoutRecord> getAllByWorkoutHistoryId(Long workoutHistoryId);

}
