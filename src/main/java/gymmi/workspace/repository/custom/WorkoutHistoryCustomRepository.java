package gymmi.workspace.repository.custom;


import gymmi.workspace.domain.entity.WorkoutHistory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkoutHistoryCustomRepository {

    List<WorkoutHistory> getAllByWorkspaceId(Long workspaceId, Pageable pageable);

}
