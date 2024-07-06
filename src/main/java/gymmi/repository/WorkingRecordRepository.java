package gymmi.repository;

import gymmi.entity.WorkingRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkingRecordRepository extends JpaRepository<WorkingRecord, Long> {

    @Query("select w from WorkingRecord w where w.worker.id = :workerId")
    List<WorkingRecord> getAllByWorkerId(Long workerId);

}
