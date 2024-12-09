package gymmi.workspace.repository;

import gymmi.exceptionhandler.legacy.NotFoundResourcesException;
import gymmi.workspace.domain.entity.WorkoutHistory;
import gymmi.workspace.repository.custom.WorkoutHistoryCustomRepository;
import gymmi.workspace.response.WorkoutConfirmationOrObjectionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkoutHistoryRepository extends JpaRepository<WorkoutHistory, Long>, WorkoutHistoryCustomRepository {

    @Query("select w from WorkoutHistory w join fetch w.workoutRecords where w.worker.id =:workerId")
    List<WorkoutHistory> getAllByWorkerId(Long workerId);

    default WorkoutHistory getByWorkoutHistoryId(Long workoutHistoryId) {
        WorkoutHistory workoutHistory = findById(workoutHistoryId)
                .orElseThrow(() -> new NotFoundResourcesException("해당 운동 기록이 존재하지 않아요."));
        return workoutHistory;
    }

    @Query("select w from WorkoutHistory w join fetch w.workoutConfirmation wf join fetch w.worker where wf.id =:workoutProofId")
    Optional<WorkoutHistory> findByWorkoutConfirmationId(Long workoutProofId);


    default WorkoutHistory getByWorkoutConfirmationId(Long workoutConfirmationId) {
        WorkoutHistory workoutHistory = findByWorkoutConfirmationId(workoutConfirmationId)
                .orElseThrow(() -> new NotFoundResourcesException("해당 운동 기록이 존재하지 않아요."));
        return workoutHistory;
    }

    @Query(nativeQuery = true,
            value = "SELECT w.id as id, w.created_at as createdAt, 'workoutHistory' as type " +
                    "FROM workout_history as w " +
                    "join worker as wo on w.worker_id = wo.id " +
                    "where wo.workspace_id =:workspaceId " +
                    "UNION ALL " +
                    "SELECT o.id as id, o.created_at as createdAt,'objection' as type " +
                    "FROM objection as o " +
                    "join worker as wo on o.worker_id = wo.id " +
                    "where wo.workspace_id =:workspaceId " +
                    "ORDER BY createdAt ASC " +
                    "LIMIT 10 OFFSET :pageNumber")
    List<Map<String, Object>> getWorkoutConfirmationAndObjection(Long workspaceId, int pageNumber);

    default List<WorkoutConfirmationOrObjectionProjection> getWorkoutConfirmationAndObjectionDto(Long workspaceId, int pageNumber) {
        pageNumber = 10 * pageNumber;
        List<Map<String, Object>> results = getWorkoutConfirmationAndObjection(workspaceId, pageNumber);
        List<WorkoutConfirmationOrObjectionProjection> dtos = new ArrayList<>();
        for (Map<String, Object> result : results) {
            dtos.add(new WorkoutConfirmationOrObjectionProjection(
                    (Long) result.get("id"), ((Timestamp) result.get("createdAt")).toLocalDateTime(), (String) result.get("type"))
            );
        }
        return dtos;
    }
}
