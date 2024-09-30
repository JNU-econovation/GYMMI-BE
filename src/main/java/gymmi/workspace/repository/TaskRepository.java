package gymmi.workspace.repository;

import gymmi.workspace.domain.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {

//    @Query("select t from Task t where t.register.id = :userId and t.workspace.id = :workspaceId")
//    Optional<Task> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);

    //    @Query("select t from Task t where t.workspace.id = :workspaceId")
//    List<Task> getAllByWorkspaceId(Long workspaceId);

    @Query("select t from Task t")
    List<Task> getAllByWorkspaceId(Long workspaceId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
//    @Query("delete from Task t where t.register.id = :userId and t.workspace.id = :workspaceId")
    @Query("delete from Task t")
    void deleteByUserIdAndWorkspaceId(Long userId, Long workspaceId);

//    @Modifying(flushAutomatically = true, clearAutomatically = true)
//    @Query("delete from Task t where t.id = (select w.task.id from Worker w where w.id = :workerId)")
//    void deleteByWorkerId(Long workerId);
}
