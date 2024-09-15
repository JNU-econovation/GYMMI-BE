package gymmi.response;

import gymmi.entity.Mission;
import gymmi.entity.WorkoutSummation;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ContributedWorkingResponse {

    private Long id;
    private String mission;
    private Integer totalCount;
    private Integer totalContributedScore;

    @Builder
    public ContributedWorkingResponse(Mission mission, WorkoutSummation workoutSummation) {
        this.id = mission.getId();
        this.mission = mission.getName();
        this.totalCount = workoutSummation.getSumOfMissionCountWorked(mission);
        this.totalContributedScore = workoutSummation.getSumOfMissionScoreContributed(mission);
    }


}
