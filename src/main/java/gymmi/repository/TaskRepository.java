package gymmi.repository;

import gymmi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t from Task t where t.user.id = :userId and t.workspace.id = :workspaceId")
    Optional<Task> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);
}
