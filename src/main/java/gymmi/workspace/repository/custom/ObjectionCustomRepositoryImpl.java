package gymmi.workspace.repository.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gymmi.workspace.domain.ObjectionStatus;
import gymmi.workspace.domain.entity.Objection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static gymmi.workspace.domain.entity.QObjection.objection;
import static gymmi.workspace.domain.entity.QVote.vote;
import static gymmi.workspace.domain.entity.QWorker.worker;

@RequiredArgsConstructor
public class ObjectionCustomRepositoryImpl implements ObjectionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Objection> getAllBy(Long workspaceId, Long workerId, ObjectionStatus objectionStatus, Pageable pageable) {
        return jpaQueryFactory.select(objection)
                .from(objection)
                .join(objection.subject, worker)
                .where(
                        inWorkspace(workspaceId),
                        objectionStatusEq(workerId, objectionStatus)
                )
                .orderBy(objection.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression objectionStatusEq(Long workerId, ObjectionStatus objectionStatus) {
        if (objectionStatus == ObjectionStatus.IN_PROGRESS) {
            return objection.isInProgress.isTrue();
        }
        if (objectionStatus == ObjectionStatus.CLOSED) {
            return objection.isInProgress.isFalse();
        }
        if (objectionStatus == ObjectionStatus.INCOMPLETION) {
            return objection.id.notIn(
                    JPAExpressions.select(vote.objection.id)
                            .from(vote)
                            .where(vote.worker.id.eq(workerId))
            );
        }
        return null;
    }

    public List<Objection> getExpiredObjections(Long workspaceId) {
        return jpaQueryFactory.select(objection)
                .from(objection)
                .join(objection.subject, worker)
                .where(
                        inWorkspace(workspaceId),
                        expiredObjection()
                )
                .fetch();
    }

    @Override
    public boolean existsByInProgress(Long workspaceId) {
        Objection result = jpaQueryFactory.select(objection)
                .from(objection)
                .join(objection.subject, worker)
                .where(
                        inWorkspace(workspaceId),
                        objection.isInProgress.isTrue()
                )
                .fetchFirst();
        return result != null;
    }

    private BooleanExpression expiredObjection() {
        return objection.isInProgress.isTrue().and(objection.createdAt.before(LocalDateTime.now().minusHours(Objection.PERIOD_HOUR)));
    }

    private BooleanExpression inWorkspace(Long workspaceId) {
        return worker.workspace.id.eq(workspaceId);
    }

}
