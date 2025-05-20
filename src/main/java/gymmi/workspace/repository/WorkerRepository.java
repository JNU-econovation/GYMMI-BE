package gymmi.workspace.repository;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.RepositoryErrorMessage;
import gymmi.exceptionhandler.message.WorkspaceErrorMessage;
import gymmi.workspace.domain.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {

    @Query("select w from Worker w where w.user.id = :userId and w.workspace.id = :workspaceId")
    Optional<Worker> findByUserIdAndWorkspaceId(Long userId, Long workspaceId);

    default Worker getByUserIdAndWorkspaceId(Long userId, Long workspaceId) {
        return findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotFoundException(RepositoryErrorMessage.NOT_FOUND_WORKER));
    }

    @Query("select w from Worker w " +
            "join w.workspace ws " +
            "join fetch w.user " +
            "where ws.id = :workspaceId " +
            "order by w.contributedScore desc, w.user.nickname asc")
    List<Worker> getAllByWorkspaceId(Long workspaceId);

}
