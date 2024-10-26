package gymmi.workspace.repository;

import gymmi.exceptionhandler.legacy.NotFoundResourcesException;
import gymmi.workspace.domain.entity.WorkoutHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkoutHistoryRepository extends JpaRepository<WorkoutHistory, Long> {

    @Query("select w from WorkoutHistory w join fetch w.workoutRecords where w.worker.id =:workerId")
    List<WorkoutHistory> getAllByWorkerId(Long workerId);

    default WorkoutHistory getById(Long workoutHistoryId) {
        WorkoutHistory workoutHistory = findById(workoutHistoryId)
                .orElseThrow(() -> new NotFoundResourcesException("해당 운동 기록이 존재하지 않아요."));
        return workoutHistory;
    }
}
