package gymmi.workspace.repository.custom;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.domain.entity.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static gymmi.workspace.domain.entity.QWorker.worker;
import static gymmi.workspace.domain.entity.QWorkspace.workspace;

@RequiredArgsConstructor
public class WorkspaceCustomRepositoryImpl implements WorkspaceCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Workspace> getAllWorkspaces(
            WorkspaceStatus status,
            String keyword,
            Pageable pageable
    ) {
        return jpaQueryFactory
                .select(workspace)
                .from(workspace)
                .where(
                        workspaceStatusEq(status),
                        workspaceNameContains(keyword)
                )
                .orderBy(workspace.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression workspaceStatusEq(WorkspaceStatus status) {
        if (status == null) {
            return defaultWorkspaceStatus();
        }
        if (status == WorkspaceStatus.COMPLETED) {
            return workspace.status.in(WorkspaceStatus.COMPLETED, WorkspaceStatus.FULLY_COMPLETED);
        }
        return workspace.status.eq(status);
    }

    private BooleanExpression defaultWorkspaceStatus() {
        return workspace.status.eq(WorkspaceStatus.IN_PROGRESS).or(
                workspace.status.eq(WorkspaceStatus.PREPARING)
        );
    }

    private BooleanExpression workspaceNameContains(String keyword) {
        if (keyword == null) {
            return null;
        }
        return workspace.name.contains(keyword);
    }

    @Override
    public List<Workspace> getJoinedWorkspacesByUserIdOrderBy_(Long userId, Pageable pageable) {
        return jpaQueryFactory.select(workspace)
                .from(worker)
                .join(worker.workspace, workspace)
                .where(worker.user.id.eq(userId))
                .orderBy(workspaceStatusOrderPriority())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    private OrderSpecifier<Integer> workspaceStatusOrderPriority() {
        return new CaseBuilder()
                .when(workspace.status.eq(WorkspaceStatus.PREPARING)).then(1)
                .when(workspace.status.eq(WorkspaceStatus.IN_PROGRESS)).then(2)
                .when(workspace.status.eq(WorkspaceStatus.COMPLETED)).then(3)
                .otherwise(4).asc();
    }

    @Override
    public Map<Workspace, Integer> getAchievementScoresIn(List<Workspace> workspaces) {
        List<Tuple> tuple = jpaQueryFactory.select(workspace, worker.contributedScore.sum())
                .from(worker)
                .join(worker.workspace, workspace)
                .where(workspace.in(workspaces))
                .groupBy(workspace)
                .fetch();
        return tuple.stream()
                .collect(Collectors.toMap(
                        t -> t.get(workspace),
                        t -> t.get(worker.contributedScore.sum())
                ));
    }

    @Override
    public long getCountsOfJoinedWorkspacesExcludeCompleted(Long userId) {
        return jpaQueryFactory.select(workspace.count())
                .from(worker)
                .join(worker.workspace, workspace)
                .where(
                        workspace.status.notIn((WorkspaceStatus.COMPLETED), WorkspaceStatus.FULLY_COMPLETED)
                                .and(worker.user.id.eq(userId)))
                .fetchOne();
    }

    /*
    @Query("select count(*) from Worker w join w.workspace ws " +
            "where (ws.status != 'COMPLETED') or ws.status != 'FULLY_COMPELTE' and w.user.id = :userId")
    long getCountsOfJoinedWorkspacesExcludeCompleted(Long userId);
     */
}
