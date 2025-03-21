package gymmi.workspace.response;

import gymmi.workspace.domain.entity.Workspace;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class JoinedWorkspaceResponse {

    private final Long id;
    private final String name;
    private final String creator;
    private final String status;
    private final String tag;
    private final LocalDateTime createdAt;
    private final Integer goalScore;
    private final Integer achievementScore;

    public JoinedWorkspaceResponse(Workspace workspace, Integer achievementScore) {
        this.id = workspace.getId();
        this.name = workspace.getName();
        this.creator = workspace.getCreator().getNickname();
        this.status = workspace.getStatus().name();
        this.tag = workspace.getTag();
        this.createdAt = workspace.getCreatedAt();
        this.goalScore = workspace.getGoalScore();
        this.achievementScore = achievementScore;
    }
}
