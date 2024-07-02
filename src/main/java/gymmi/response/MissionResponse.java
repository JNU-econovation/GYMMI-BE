package gymmi.response;

import gymmi.entity.Mission;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionResponse {

    private Long id;
    private String mission;
    private Integer score;

    public MissionResponse(Mission mission) {
        this.id = mission.getId();
        this.mission = mission.getName();
        this.score = mission.getScore();
    }

}
