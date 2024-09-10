package gymmi.response;

import gymmi.entity.Workspace;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkspaceResponse {

    private final Long id;
    private final String name;
    private final String status;
    private final Integer goalScore;
    private final Integer achievementScore;
    private final LocalDateTime createdAt;

    @Builder
    public WorkspaceResponse(Long id, String name, String status, Integer goalScore, Integer achievementScore,
                             LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.goalScore = goalScore;
        this.achievementScore = achievementScore;
        this.createdAt = createdAt;
    }

    public WorkspaceResponse(Workspace workspace, Integer achievementScore) {
        this.id = workspace.getId();
        this.name = workspace.getName();
        this.status = workspace.getStatus().name();
        this.goalScore = workspace.getGoalScore();
        this.achievementScore = achievementScore;
        this.createdAt = workspace.getCreatedAt();
    }

}
