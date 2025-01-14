package gymmi.workspace.repository;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.repository.custom.WorkspaceCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long>, WorkspaceCustomRepository {

    boolean existsByName(String name);

    default Workspace getWorkspaceById(Long id) {
        Workspace workspace = findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_WORKSPACE));
        return workspace;
    }

    @Query("select sum(w.contributedScore) from Worker w join w.workspace ws where ws.id = :workspaceId")
    int getAchievementScore(Long workspaceId);

    @Query("select max(w.contributedScore) From Worker w join w.workspace ws where ws.id = :workspaceId")
    int getFirstPlaceScoreIn(Long workspaceId);

}
