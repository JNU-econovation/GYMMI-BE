package gymmi.workspace.repository;

import gymmi.workspace.domain.FavoriteMission;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteMissionRepository extends JpaRepository<FavoriteMission, Long> {


    Optional<FavoriteMission> findByWorkerIdAndMissionId(Long workerId, Long missionId);

}
