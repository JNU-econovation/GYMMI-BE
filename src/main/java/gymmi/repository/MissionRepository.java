package gymmi.repository;

import gymmi.entity.Mission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from Mission m where m.workspace.id = :workspaceId")
    void deleteAllByWorkspaceId(Long workspaceId);

    @Query("select m from Mission m where m.workspace.id = :workspaceId")
    List<Mission> getAllByWorkspaceId(Long workspaceId);
}
