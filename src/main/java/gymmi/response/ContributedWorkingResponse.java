package gymmi.response;

import gymmi.entity.Mission;
import gymmi.entity.WorkingSummation;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ContributedWorkingResponse {

    private Long id;
    private String mission;
    private Integer totalCount;
    private Integer totalContributedScore;

    @Builder
    public ContributedWorkingResponse(Mission mission, WorkingSummation workingSummation) {
        this.id = mission.getId();
        this.mission = mission.getName();
        this.totalCount = workingSummation.getSumOfMissionCountWorked(mission);
        this.totalContributedScore = workingSummation.getSumOfMissionScoreContributed(mission);
    }


}
