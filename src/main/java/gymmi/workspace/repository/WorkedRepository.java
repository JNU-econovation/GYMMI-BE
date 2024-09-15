package gymmi.workspace.repository;

import gymmi.workspace.domain.Worked;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkedRepository extends JpaRepository<Worked, Long> {

    @Query("select w from Worked w join fetch w.workoutRecords where w.worker.id =:workerId")
    List<Worked> getAllByWorkerId(Long workerId);
}
