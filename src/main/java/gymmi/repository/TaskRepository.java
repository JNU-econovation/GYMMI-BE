package gymmi.repository;

import gymmi.entity.Mission;
import gymmi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
