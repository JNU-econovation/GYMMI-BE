package gymmi.workspace.repository;

import gymmi.workspace.domain.WorkoutRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkoutRecordRepository extends JpaRepository<WorkoutRecord, Long> {

    @Query("select w from WorkoutRecord w where w.worked.worker.id = :workerId")
    List<WorkoutRecord> getAllByWorkerId(Long workerId);

}
