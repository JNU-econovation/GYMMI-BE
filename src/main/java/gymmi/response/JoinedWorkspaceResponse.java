package gymmi.response;

import java.time.LocalDateTime;
import lombok.Builder;
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

    @Builder
    public JoinedWorkspaceResponse(Long id, String name, String creator, String status, String tag,
                                   LocalDateTime createdAt, Integer goalScore, Integer achievementScore) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.status = status;
        this.tag = tag;
        this.createdAt = createdAt;
        this.goalScore = goalScore;
        this.achievementScore = achievementScore;
    }
}
