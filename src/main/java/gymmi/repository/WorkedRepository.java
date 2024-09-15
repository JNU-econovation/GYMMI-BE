package gymmi.repository;

import gymmi.entity.Worked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkedRepository extends JpaRepository<Worked, Long> {

    @Query("select w from Worked w join fetch w.workoutRecords where w.worker.id =:workerId")
    List<Worked> getAllByWorkerId(Long workerId);
}
