package gymmi.workspace.repository.custom;

import gymmi.workspace.domain.Workspace;
import gymmi.workspace.domain.WorkspaceStatus;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface WorkspaceCustomRepository {

    List<Workspace> getAllWorkspaces(WorkspaceStatus status, String keyword, Pageable pageable);

    List<Workspace> getJoinedWorkspacesByUserIdOrderBy_(Long userId, Pageable pageable);

    Map<Workspace, Integer> getAchievementScoresIn(List<Workspace> workspaces);

    long getCountsOfJoinedWorkspacesExcludeCompleted(Long userId);
}
