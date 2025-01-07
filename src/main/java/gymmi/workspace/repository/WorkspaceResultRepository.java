package gymmi.workspace.repository;

import gymmi.exceptionhandler.legacy.NotFoundResourcesException;
import gymmi.workspace.domain.entity.WorkspaceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkspaceResultRepository extends JpaRepository<WorkspaceResult, Long> {

    default WorkspaceResult getByWorkspaceId(Long workspaceId) {
        WorkspaceResult workspaceResult = findByWorkspaceId(workspaceId)
                .orElseThrow(() -> new NotFoundResourcesException("해당 결과가 존재하지 않아요."));
        return workspaceResult;
    }

    @Query("select w from WorkspaceResult w where w.workspace.id =:workspaceId")
    Optional<WorkspaceResult> findByWorkspaceId(Long workspaceId);
}
