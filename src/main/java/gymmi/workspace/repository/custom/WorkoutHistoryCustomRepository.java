package gymmi.workspace.repository.custom;


import gymmi.workspace.domain.entity.WorkoutHistory;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface WorkoutHistoryCustomRepository {

    List<WorkoutHistory> getAllByWorkspaceId(Long workspaceId, Pageable pageable);

    List<WorkoutHistory> getAllByDate(LocalDate localDate);
}
