package gymmi.workspace.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gymmi.entity.QUser;
import gymmi.workspace.domain.entity.QWorker;
import gymmi.workspace.domain.entity.QWorkoutProof;
import gymmi.workspace.domain.entity.WorkoutHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static gymmi.entity.QUser.*;
import static gymmi.workspace.domain.entity.QWorker.worker;
import static gymmi.workspace.domain.entity.QWorkoutHistory.workoutHistory;
import static gymmi.workspace.domain.entity.QWorkoutProof.*;

@RequiredArgsConstructor
public class WorkoutHistoryCustomRepositoryImpl implements WorkoutHistoryCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<WorkoutHistory> getAllByWorkspaceId(Long workspaceId, Pageable pageable) {
        return jpaQueryFactory.select(workoutHistory)
                .from(workoutHistory)
                .join(workoutHistory.workoutProof, workoutProof).fetchJoin()
                .join(workoutHistory.worker, worker).fetchJoin()
                .where(worker.workspace.id.eq(workspaceId))
                .orderBy(workoutHistory.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
