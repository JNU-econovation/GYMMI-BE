package gymmi.workspace.mission.domain;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.mission.domain.entity.Mission;
import gymmi.workspace.workspace.domain.entity.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MissionExistenceValidator {

    public static void validateMissionExistenceInWorkspace(Workspace workspace, List<Mission> missions) {
        if (!missions.stream().allMatch(mission -> mission.isRegisteredIn(workspace))) {
            throw new NotFoundException(ErrorCode.NOT_REGISTERED_WORKSPACE_MISSION);
        }
    }

    public static void validateMissionExistenceInWorkspace(Workspace workspace, Mission... mission) {
        validateMissionExistenceInWorkspace(workspace, Arrays.asList(mission));
    }

}
