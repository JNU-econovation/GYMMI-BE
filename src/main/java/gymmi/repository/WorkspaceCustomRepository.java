package gymmi.repository;

import gymmi.entity.Workspace;
import gymmi.entity.WorkspaceStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkspaceCustomRepository {

    List<Workspace> getAllWorkspaces(WorkspaceStatus status, String keyword, Pageable pageable);

}
