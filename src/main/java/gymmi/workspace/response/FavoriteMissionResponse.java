package gymmi.workspace.response;

import gymmi.workspace.domain.entity.Mission;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for test
public class FavoriteMissionResponse {

    private Long id;
    private String mission;
    private Integer score;

    public FavoriteMissionResponse(Mission mission) {
        this.id = mission.getId();
        this.mission = mission.getName();
        this.score = mission.getScore();
    }

}
