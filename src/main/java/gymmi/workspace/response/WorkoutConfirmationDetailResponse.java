package gymmi.workspace.response;

import gymmi.entity.User;
import lombok.Getter;

@Getter
public class WorkoutConfirmationDetailResponse {

    private final String nickname;
    private final String loginId;
    private final String profileImageUrl;
    private final String workoutConfirmationImageUrl;
    private final String comment;

    public WorkoutConfirmationDetailResponse(User user, String workoutConfirmationImageUrl, String comment) {
        this.nickname = user.getNickname();
        this.loginId = user.getLoginId();
        this.profileImageUrl = user.getProfileImageName();
        this.workoutConfirmationImageUrl = workoutConfirmationImageUrl;
        this.comment = comment;
    }
}
