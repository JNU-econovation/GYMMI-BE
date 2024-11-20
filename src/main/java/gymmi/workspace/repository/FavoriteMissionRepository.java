package gymmi.workspace.repository;

import gymmi.workspace.domain.entity.FavoriteMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteMissionRepository extends JpaRepository<FavoriteMission, Long> {

    Optional<FavoriteMission> findByWorkerIdAndMissionId(Long workerId, Long missionId);

    @Query("select fm from FavoriteMission fm join fetch fm.mission where fm.worker.id = :workerId")
    List<FavoriteMission> getAllByWorkerId(Long workerId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from FavoriteMission fm where fm.worker.id = :workerId")
    void deleteAllByWorkerId(Long workerId);
}
