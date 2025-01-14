package gymmi.workspace.repository;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.WorkspaceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkspaceResultRepository extends JpaRepository<WorkspaceResult, Long> {

    default WorkspaceResult getByWorkspaceId(Long workspaceId) {
        WorkspaceResult workspaceResult = findByWorkspaceId(workspaceId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_WORKSPACE_RESULT));
        return workspaceResult;
    }

    @Query("select w from WorkspaceResult w where w.workspace.id =:workspaceId")
    Optional<WorkspaceResult> findByWorkspaceId(Long workspaceId);
}
