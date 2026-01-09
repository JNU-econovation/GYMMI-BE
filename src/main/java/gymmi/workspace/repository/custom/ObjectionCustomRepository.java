package gymmi.workspace.repository.custom;

import gymmi.workspace.service.domain.objection.ObjectionStatus;
import gymmi.workspace.service.domain.objection.Objection;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ObjectionCustomRepository {

    List<Objection> getAllBy(Long workspaceId, Long workerId, ObjectionStatus objectionStatus, Pageable pageable);

    List<Objection> getExpiredObjections(Long workspaceId);

    boolean existsByInProgress(Long workspaceId);

}
