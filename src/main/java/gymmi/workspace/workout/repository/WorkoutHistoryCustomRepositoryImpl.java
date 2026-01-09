package gymmi.workspace.workout.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static gymmi.workspace.service.domain.workout.QWorkoutConfirmation.workoutConfirmation;
import static gymmi.workspace.service.domain.workout.QWorkoutHistory.workoutHistory;
import static gymmi.workspace.service.domain.workspace.QWorker.worker;


@RequiredArgsConstructor
public class WorkoutHistoryCustomRepositoryImpl implements WorkoutHistoryCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<WorkoutHistory> getAllByWorkspaceId(Long workspaceId, Pageable pageable) {
        return jpaQueryFactory.select(workoutHistory)
                .from(workoutHistory)
                .join(workoutHistory.workoutConfirmation, workoutConfirmation).fetchJoin()
                .join(workoutHistory.worker, worker).fetchJoin()
                .where(worker.workspace.id.eq(workspaceId))
                .orderBy(workoutHistory.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<WorkoutHistory> getAllByDate(LocalDate now) {
        LocalDateTime startDay = now.atStartOfDay();
        LocalDateTime endDay = now.plusDays(1).atStartOfDay();
        return jpaQueryFactory.select(workoutHistory)
                .from(workoutHistory)
                .where(workoutHistory.createdAt.goe(startDay).and(
                        workoutHistory.createdAt.before(endDay)
                ))
                .fetch();
    }

    @Override
    public List<WorkoutHistory> findTodayByWorkerId(Long workerId) {
        LocalDate now = LocalDate.now();
        LocalDateTime startDay = now.atStartOfDay();
        LocalDateTime endDay = now.plusDays(1).atStartOfDay();
        return jpaQueryFactory.select(workoutHistory)
                .from(workoutHistory)
                .where(workoutHistory.createdAt.goe(startDay).and(workoutHistory.createdAt.before(endDay)
                        .and(workoutHistory.worker.id.eq(workerId))
                ))
                .fetch();
    }
}
