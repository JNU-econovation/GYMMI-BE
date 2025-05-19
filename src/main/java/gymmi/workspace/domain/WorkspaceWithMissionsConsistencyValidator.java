package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.workspace.domain.entity.Mission;
import gymmi.workspace.domain.entity.Workspace;
import java.util.List;
import lombok.Getter;

@Getter
public class WorkspaceWithMissionsConsistencyValidator {
    private WorkspaceWithMissionsConsistencyValidator() {
    }

    public static void validateRegistration(Workspace workspace, List<Mission> missions) {
        if (!missions.stream()
                .allMatch(mission -> mission.isRegisteredIn(workspace))) {
            throw new InvalidStateException(ErrorMessage.EXIST_NOT_REGISTERED_MISSION);
        }
    }

    public static void validateConsistencyMissionsCount(List<Mission> missions) {
        if (missions.isEmpty() || missions.size() > WorkspaceInitializer.MAX_MISSIONS_SIZE) {
            throw new InvalidStateException(ErrorMessage.NOT_CONSISTENT_MISSIONS_COUNT);
        }
    }

}
