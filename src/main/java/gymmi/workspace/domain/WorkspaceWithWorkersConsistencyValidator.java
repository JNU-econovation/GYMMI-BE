package gymmi.workspace.domain;

import static gymmi.exception.message.ErrorCode.EXIST_NOT_JOINED_WORKER;
import static gymmi.exception.message.ErrorCode.NOT_CONSISTENT_WORKERS;

import gymmi.exception.class1.InvalidStateException;
import java.util.List;
import lombok.Getter;

@Getter
public class WorkspaceWithWorkersConsistencyValidator {
    private WorkspaceWithWorkersConsistencyValidator() {
    }

    public static void validateMeetMinHeadCount(List<Worker> workers) {
        if (workers.size() < Workspace.MIN_HEAD_COUNT) {
            throw new InvalidStateException(NOT_CONSISTENT_WORKERS);
        }
    }

    public static void validateWorkersConsistency(Workspace workspace, List<Worker> workers) {
        if (!workers.stream()
                .allMatch(worker -> worker.isJoinedIn(workspace))) {
            throw new InvalidStateException(EXIST_NOT_JOINED_WORKER);
        }
        if (workers.isEmpty() || workers.size() > workspace.getHeadCount()) {
            throw new InvalidStateException(NOT_CONSISTENT_WORKERS);
        }
    }

}
