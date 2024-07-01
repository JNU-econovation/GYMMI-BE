package gymmi.response;

import gymmi.entity.Mission;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MissionResponse {

    private final Long id;
    private final String mission;
    private final Integer score;

    public MissionResponse(Mission mission) {
        this.id = mission.getId();
        this.mission = mission.getName();
        this.score = mission.getScore();
    }

}
