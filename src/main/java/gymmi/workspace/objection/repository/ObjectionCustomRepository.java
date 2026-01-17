package gymmi.workspace.objection.repository;

import gymmi.workspace.objection.domain.ObjectionStatus;
import gymmi.workspace.objection.domain.entity.Objection;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ObjectionCustomRepository {

    List<Objection> getAllBy(Long workspaceId, Long workerId, ObjectionStatus objectionStatus, Pageable pageable);

    List<Objection> getExpiredObjections(Long workspaceId);

    boolean existsByInProgress(Long workspaceId);

}
