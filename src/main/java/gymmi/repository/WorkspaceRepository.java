package gymmi.repository;

import gymmi.entity.Workspace;
import gymmi.exception.NotFoundResourcesException;
import gymmi.repository.custom.WorkspaceCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long>, WorkspaceCustomRepository {

    boolean existsByName(String name);

    default Workspace getWorkspaceById(Long id) {
        Workspace workspace = findById(id)
                .orElseThrow(() -> new NotFoundResourcesException("해당 워크스페이스가 존재하지 않습니다."));
        return workspace;
    }

    @Query("select count(*) from Worker w join w.workspace ws " +
            "where ws.status != 'COMPLETED' and w.user.id = :userId")
    long getCountsOfJoinedWorkspacesExcludeCompleted(Long userId);


    @Query("select sum(w.contributedScore) from Worker w join w.workspace ws where ws.id = :workspaceId")
    int getAchievementScore(Long workspaceId);

}
