package gymmi.repository;

import gymmi.entity.WorkoutRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkoutRecordRepository extends JpaRepository<WorkoutRecord, Long> {

    @Query("select w from WorkoutRecord w where w.worked.worker.id = :workerId")
    List<WorkoutRecord> getAllByWorkerId(Long workerId);

}
