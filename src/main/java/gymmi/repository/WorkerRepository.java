package gymmi.repository;

import gymmi.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {

    @Query("select w from Worker w where w.user.id = :userId and w.workspace.id = :workspaceId")
    Optional<Worker> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);

    @Query("select count(w) from Worker w where w.workspace.id = :workspaceId")
    Integer countAllByWorkspaceId(Long workspaceId);


//  boolean으로 가능?? ->
//    @Query(value = "select * from worker w", nativeQuery = true)
//    boolean findByUserIdWorkspaceId(Long userId, Long workspaceId);
}
