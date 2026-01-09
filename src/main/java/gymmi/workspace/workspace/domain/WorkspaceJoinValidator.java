package gymmi.workspace.workspace.domain;

import gymmi.global.exceptionhandler.exception.InvalidStateException;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class WorkspaceJoinValidator {

    public static final int MAXIMUM_WORKSPACE_COUNT = 5;

    public static void validateWorkspaceCountLimit(int countOfJoinedWorkspaces) {
        if (hasReachedLimit(countOfJoinedWorkspaces)) {
            throw new InvalidStateException(ErrorCode.EXCEED_MAX_JOINED_WORKSPACE);
        }
    }

    private static boolean hasReachedLimit(int countOfJoinedWorkspaces) {
        return countOfJoinedWorkspaces >= MAXIMUM_WORKSPACE_COUNT;
    }

}
