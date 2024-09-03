package gymmi.repository;

import gymmi.entity.Mission;
import gymmi.exception.NotFoundResourcesException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Mission m where m.workspace.id = :workspaceId")
    void deleteAllByWorkspaceId(Long workspaceId);

    @Query("select m from Mission m where m.workspace.id = :workspaceId order by m.name asc")
    List<Mission> getAllByWorkspaceId(Long workspaceId);

    @Query("select m from Mission m where m.id = :missionId")
    Optional<Mission> findByMissionId(Long missionId);

    default Mission getByMissionId(Long missionId) {
        Mission mission = findByMissionId(missionId)
                .orElseThrow(() -> new NotFoundResourcesException("해당 미션이 존재하지 않아요."));
        return mission;
    }
}
