package gymmi.workspace.workspace.repository;

import gymmi.global.exception.exceptiontype.NotFoundException;
import gymmi.global.exception.message.ErrorCode;
import gymmi.workspace.workspace.domain.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long>, WorkspaceCustomRepository {

    boolean existsByName(String name);

    default Workspace findByIdOrThrow(Long id) {
        Workspace workspace = findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_WORKSPACE));
        return workspace;
    }

    @Query("select sum(w.contributedScore) from Worker w join w.workspace ws where ws.id = :workspaceId")
    int getAchievementScore(Long workspaceId);

    @Query("select max(w.contributedScore) From Worker w join w.workspace ws where ws.id = :workspaceId")
    int getFirstPlaceScoreIn(Long workspaceId);

}
