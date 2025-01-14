package gymmi.workspace.repository;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Mission;
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
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MISSION));
        return mission;
    }
}
