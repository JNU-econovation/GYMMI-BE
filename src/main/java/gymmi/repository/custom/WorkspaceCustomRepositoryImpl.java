package gymmi.repository.custom;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gymmi.entity.Workspace;
import gymmi.entity.WorkspaceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.*;
import static gymmi.entity.QWorker.worker;
import static gymmi.entity.QWorkspace.workspace;

@RequiredArgsConstructor
@Repository
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
        return jpaQueryFactory.select(workspace, worker.contributedScore.sum())
                .from(worker)
                .join(worker.workspace, workspace)
                .where(workspace.in(workspaces))
                .orderBy()
                .transform(groupBy(workspace).as(worker.contributedScore.sum()));
    }
}
