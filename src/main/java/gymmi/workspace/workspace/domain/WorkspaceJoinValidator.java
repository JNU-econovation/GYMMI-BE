package gymmi.workspace.workspace.domain;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkspaceJoinValidator {

    public static final int MAXIMUM_WORKSPACE_COUNT = 5;

    private final WorkspaceRepository workspaceRepository;

    public void validateWorkspaceCountLimit(Long userId) {
        long countOfJoinedWorkspaces = workspaceRepository.getCountsOfJoinedWorkspacesExcludeCompleted(userId);
        if (hasReachedLimit(countOfJoinedWorkspaces)) {
            throw new InvalidStateException(ErrorCode.EXCEED_MAX_JOINED_WORKSPACE);
        }
    }

    private boolean hasReachedLimit(long countOfJoinedWorkspaces) {
        return countOfJoinedWorkspaces >= MAXIMUM_WORKSPACE_COUNT;
    }

}
