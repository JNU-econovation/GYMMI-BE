package gymmi.workspace.workspace.domain;

import gymmi.global.exception.exceptiontype.InvalidStateException;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;

import static gymmi.global.exception.message.ErrorCode.NOT_JOINED_WORKSPACE;

public class ParticipantValidator {

    public static void validateParticipant(Workspace workspace, Worker worker) {
        if (!worker.isJoinedIn(workspace)) {
            throw new InvalidStateException(NOT_JOINED_WORKSPACE);
        }
    }
}
