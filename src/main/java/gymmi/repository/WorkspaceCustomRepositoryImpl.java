package gymmi.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gymmi.entity.QWorkspace;
import gymmi.entity.Workspace;
import gymmi.entity.WorkspaceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        QWorkspace workspace = new QWorkspace("workspace");
        return jpaQueryFactory
                .select(workspace)
                .from(workspace)
                .where(
                        workspaceStatusEq(workspace, status),
                        keywordEq(workspace, keyword)
                )
                .orderBy(workspace.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression workspaceStatusEq(QWorkspace workspace, WorkspaceStatus status) {
        return status == null ? workspacesStatusNotEqCompleted(workspace) : workspace.status.eq(status);
    }

    private BooleanExpression workspacesStatusNotEqCompleted(QWorkspace workspace) {
        return workspace.status.eq(WorkspaceStatus.IN_PROGRESS).or(workspace.status.eq(WorkspaceStatus.PREPARING));
    }

    private BooleanExpression keywordEq(QWorkspace workspace, String keyword) {
        return keyword == null ? null : workspace.name.contains(keyword);
    }
}
