package gymmi.repository.custom;

import gymmi.entity.Workspace;
import gymmi.entity.WorkspaceStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface WorkspaceCustomRepository {

    List<Workspace> getAllWorkspaces(WorkspaceStatus status, String keyword, Pageable pageable);

    List<Workspace> getJoinedWorkspacesByUserIdOrderBy_(Long userId, Pageable pageable);

    Map<Workspace, Integer> getAchievementScoresIn(List<Workspace> workspaces);
}
