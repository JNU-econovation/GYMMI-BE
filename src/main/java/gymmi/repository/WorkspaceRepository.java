package gymmi.repository;


import gymmi.entity.Workspace;
import gymmi.exception.NotFoundResourcesException;
import gymmi.repository.custom.WorkspaceCustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long>, WorkspaceCustomRepository {

    boolean existsByName(String name);

    default Workspace getWorkspaceById(Long id) {
        Workspace workspace = findById(id)
                .orElseThrow(() -> new NotFoundResourcesException("해당 워크스페이스가 존재하지 않습니다."));
        return workspace;
    }

    @Query(value = "select ws.id, ws.creator, ws.created_at, ws.head_count, ws.description, " +
            "ws.goal_score, ws.tag, ws.name, ws.password, ws.status from workspace ws inner join worker w on ws.id = w.workspace_id where w.user_id = :userId "
            +
            "order by case " +
            "when ws.status = 'PREPARING' then 1 " +
            "when ws.status = 'IN_PROGRESS' then 2 " +
            "when ws.status = 'COMPLETED' then 3 " +
            "end, w.created_at DESC " +
            "limit 10 offset :pageNumber", nativeQuery = true)
    List<Workspace> getJoinedWorkspacesByUserIdOrderBy_(Long userId, int pageNumber);

    @Query("select count(*) from Workspace ws join Worker w on ws.id = w.workspace.id " +
            "where (ws.status = 'PREPARING' or ws.status = 'IN_PROGRESS') " +
            "and w.user.id = :userId")
    int getCountsOfJoinedWorkspacesWhereStatusIsPreparingOrInProgress(Long userId);


    @Query("select sum(w.contributedScore) from Workspace ws inner join Worker w on ws.id = w.workspace.id where ws.id = :workspaceId")
    int getAchievementScore(Long workspaceId);

}
