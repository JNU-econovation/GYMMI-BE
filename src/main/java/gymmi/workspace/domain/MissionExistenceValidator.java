package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Mission;
import gymmi.workspace.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MissionExistenceValidator {

    private final MissionRepository missionRepository;

    public void validateMissionExistenceInWorkspace(Long workspaceId, List<Long> missionIds) {
        List<Long> missions = missionRepository.getAllByWorkspaceId(workspaceId)
                .stream()
                .map(Mission::getId)
                .toList();

        if (!missions.containsAll(missionIds)) {
            throw new NotFoundException(ErrorCode.NOT_REGISTERED_WORKSPACE_MISSION);
        }
    }

    public void validateMissionExistenceInWorkspace(Long workspaceId, Long... missionIds) {
        validateMissionExistenceInWorkspace(workspaceId, Arrays.asList(missionIds));
    }
}
