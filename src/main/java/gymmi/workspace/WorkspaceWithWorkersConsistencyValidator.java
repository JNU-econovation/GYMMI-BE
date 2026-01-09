package gymmi.workspace;

import static gymmi.exceptionhandler.message.ErrorCode.EXIST_NOT_JOINED_WORKER;
import static gymmi.exceptionhandler.message.ErrorCode.NOT_CONSISTENT_WORKERS_COUNT;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import java.util.List;

import gymmi.workspace.workspace.domain.WorkspaceCreationValidator;
import lombok.Getter;

@Getter
public class WorkspaceWithWorkersConsistencyValidator {
    private WorkspaceWithWorkersConsistencyValidator() {
    }

    public static void validateMeetMinHeadCount(List<Worker> workers) {
        if (workers.size() < WorkspaceCreationValidator.MIN_HEAD_COUNT) {
            throw new InvalidStateException(NOT_CONSISTENT_WORKERS_COUNT);
        }
    }

    public static void validateWorkersConsistency(Workspace workspace, List<Worker> workers) {
        if (!workers.stream()
                .allMatch(worker -> worker.isJoinedIn(workspace))) {
            throw new InvalidStateException(EXIST_NOT_JOINED_WORKER);
        }
        if (workers.isEmpty() || workers.size() > workspace.getHeadCount()) {
            throw new InvalidStateException(NOT_CONSISTENT_WORKERS_COUNT);
        }
    }

}
