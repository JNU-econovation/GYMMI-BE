package gymmi.workspace.repository;

import gymmi.exceptionhandler.legacy.NotFoundResourcesException;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.repository.custom.WorkspaceCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long>, WorkspaceCustomRepository {

    boolean existsByName(String name);

    default Workspace getWorkspaceById(Long id) {
        Workspace workspace = findById(id)
                .orElseThrow(() -> new NotFoundResourcesException("해당 워크스페이스가 존재하지 않습니다."));
        return workspace;
    }

    @Query("select sum(w.contributedScore) from Worker w join w.workspace ws where ws.id = :workspaceId")
    int getAchievementScore(Long workspaceId);

    @Query("select max(w.contributedScore) From Worker w join w.workspace ws where ws.id = :workspaceId")
    int getFirstPlaceScoreIn(Long workspaceId);

}
