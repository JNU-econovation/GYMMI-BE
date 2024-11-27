package gymmi.workspace.repository.custom;

import gymmi.workspace.domain.ObjectionStatus;
import gymmi.workspace.domain.entity.Objection;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ObjectionCustomRepository {

    List<Objection> getAllBy(Long workspaceId, Long workerId, ObjectionStatus objectionStatus, Pageable pageable);

}
