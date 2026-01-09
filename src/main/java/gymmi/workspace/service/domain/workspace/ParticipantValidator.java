package gymmi.workspace.service.domain.workspace;

import gymmi.exceptionhandler.exception.InvalidStateException;

import static gymmi.exceptionhandler.message.ErrorCode.NOT_JOINED_WORKSPACE;

public class ParticipantValidator {

    public static void validateParticipant(Workspace workspace, Worker worker) {
        if (!worker.isJoinedIn(workspace)) {
            throw new InvalidStateException(NOT_JOINED_WORKSPACE);
        }
    }
}
