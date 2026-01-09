package gymmi.workspace.mission.domain;

import gymmi.exceptionhandler.exception.InvalidRangeException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.mission.domain.entity.Mission;

import java.util.ArrayList;
import java.util.List;

public class Missions {

    private final List<Mission> missions;

    public Missions(List<Mission> missions) {
        validateSize(missions);
        this.missions = new ArrayList<>(missions);
    }

    public List<Mission> getMissions() {
        return missions;
    }

    private void validateSize(List<Mission> missions) {
        if (missions.isEmpty() || missions.size() > 15) {
            throw new InvalidRangeException(ErrorCode.INVALID_WORKSPACE_MISSION_SIZE);
        }
    }
}
