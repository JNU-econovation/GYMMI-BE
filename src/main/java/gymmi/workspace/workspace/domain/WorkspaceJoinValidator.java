package gymmi.workspace.workspace.domain;

import gymmi.global.exception.exceptiontype.InvalidStateException;
import gymmi.global.exception.message.ErrorCode;
import lombok.RequiredArgsConstructor;

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
