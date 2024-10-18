package gymmi.workspace.repository;

import gymmi.exception.class1.NotFoundException;
import gymmi.exception.message.ErrorCode;
import gymmi.workspace.domain.Worker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WorkerRepository extends JpaRepository<Worker, Long> {

    @Query("select w from Worker w where w.user.id = :userId and w.workspace.id = :workspaceId")
    Optional<Worker> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);

    default Worker getByUserIdAndWorkspaceId(Long userId, Long workspaceId) {
        return findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_WORKER));
    }

    @Query("select w from Worker w " +
            "join w.workspace ws " +
            "join fetch w.user " +
            "where ws.id = :workspaceId " +
            "order by w.contributedScore desc, w.user.nickname asc")
    List<Worker> getAllByWorkspaceId(Long workspaceId);

}
