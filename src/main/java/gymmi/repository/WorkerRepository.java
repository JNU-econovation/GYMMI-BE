package gymmi.repository;

import gymmi.entity.Worker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WorkerRepository extends JpaRepository<Worker, Long> {

    @Query("select w from Worker w where w.user.id = :userId and w.workspace.id = :workspaceId")
    Optional<Worker> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);

    @Query("select count(w) from Worker w where w.workspace.id = :workspaceId")
    Integer countAllByWorkspaceId(Long workspaceId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Worker w where w.user.id = :userId and w.workspace.id = :workspaceId")
    void deleteByUserIdAndWorkspaceId(Long userId, Long workspaceId);

    @Query("select w from Worker w join w.workspace ws where ws.id = :workspaceId order by w.contributedScore desc, w.user.id asc")
    List<Worker> getAllByWorkspaceIdOrderByContributedScore(Long workspaceId);

//  boolean으로 가능?? ->
//    @Query(value = "select * from worker w", nativeQuery = true)
//    boolean findByUserIdWorkspaceId(Long userId, Long workspaceId);
}
