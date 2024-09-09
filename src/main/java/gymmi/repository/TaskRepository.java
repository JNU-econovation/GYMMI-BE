package gymmi.repository;

import gymmi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
}
