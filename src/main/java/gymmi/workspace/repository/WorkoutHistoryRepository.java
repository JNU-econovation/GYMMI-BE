package gymmi.workspace.repository;

import gymmi.exceptionhandler.legacy.NotFoundResourcesException;
import gymmi.workspace.domain.entity.WorkoutHistory;
import gymmi.workspace.repository.custom.WorkoutHistoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkoutHistoryRepository extends JpaRepository<WorkoutHistory, Long>, WorkoutHistoryCustomRepository {

    @Query("select w from WorkoutHistory w join fetch w.workoutRecords where w.worker.id =:workerId")
    List<WorkoutHistory> getAllByWorkerId(Long workerId);

    default WorkoutHistory getByWorkoutHistoryId(Long workoutHistoryId) {
        WorkoutHistory workoutHistory = findById(workoutHistoryId)
                .orElseThrow(() -> new NotFoundResourcesException("해당 운동 기록이 존재하지 않아요."));
        return workoutHistory;
    }

    @Query("select w from WorkoutHistory w join fetch w.workoutProof wf join fetch w.worker where wf.id =:workoutProofId")
    Optional<WorkoutHistory> findByWorkoutProofId(Long workoutProofId);


    default WorkoutHistory getByWorkoutProofId(Long workoutProofId) {
        WorkoutHistory workoutHistory = findByWorkoutProofId(workoutProofId)
                .orElseThrow(() -> new NotFoundResourcesException("해당 운동 기록이 존재하지 않아요."));
        return workoutHistory;
    }
}
