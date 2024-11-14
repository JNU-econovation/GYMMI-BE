package gymmi.workspace.response;

import gymmi.entity.User;
import gymmi.workspace.domain.entity.Objection;
import lombok.Getter;

@Getter
public class WorkoutConfirmationDetailResponse {

    private final String nickname;
    private final String loginId;
    private final String profileImageUrl;
    private final String workoutConfirmationImageUrl;
    private final String comment;
    private final Long objectionId;

    public WorkoutConfirmationDetailResponse(User user, String workoutConfirmationImageUrl, String comment, Objection objection) {
        this.nickname = user.getNickname();
        this.loginId = user.getLoginId();
        this.profileImageUrl = user.getProfileImageName();
        this.workoutConfirmationImageUrl = workoutConfirmationImageUrl;
        this.comment = comment;
        this.objectionId = getObjectionId(objection);
    }

    private Long getObjectionId(Objection objection) {
        if (objection == null) {
            return null;
        }
        return objection.getId();
    }
}
