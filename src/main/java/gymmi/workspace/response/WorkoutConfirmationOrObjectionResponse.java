package gymmi.workspace.response;

import gymmi.entity.User;
import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.domain.entity.WorkoutHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkoutConfirmationOrObjectionResponse {

    private final Long objectionId;
    private final Long workoutConfirmationId;
    private final String nickname;
    private final String profileImageUrl;
    private final String workoutConfirmationImageUrl;
    private final LocalDateTime createdAt;
    private final String dayOfTheWeek;
    private final Boolean isMine;
    private final Boolean isObjection;

    @Builder
    public WorkoutConfirmationOrObjectionResponse(
            Long objectionId, Long workoutConfirmationId, String nickname, String profileImageUrl, String workoutConfirmationImageUrl,
            LocalDateTime createdAt, String dayOfTheWeek, Boolean isMine, Boolean isObjection
    ) {
        this.objectionId = objectionId;
        this.workoutConfirmationId = workoutConfirmationId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.workoutConfirmationImageUrl = workoutConfirmationImageUrl;
        this.createdAt = createdAt;
        this.dayOfTheWeek = dayOfTheWeek;
        this.isMine = isMine;
        this.isObjection = isObjection;
    }

    public static WorkoutConfirmationOrObjectionResponse workoutConfirmation(User loginedUser, Objection objection, WorkoutHistory workoutHistory, String workoutConfirmationImageUrl) {
        User user = workoutHistory.getWorker().getUser();
        LocalDateTime createdAt = workoutHistory.getCreatedAt();
        return WorkoutConfirmationOrObjectionResponse.builder()
                .workoutConfirmationId(workoutHistory.getWorkoutConfirmation().getId())
                .objectionId(getObjectId(objection))
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageName())
                .workoutConfirmationImageUrl(workoutConfirmationImageUrl)
                .createdAt(createdAt)
                .dayOfTheWeek(createdAt.getDayOfWeek().name())
                .isObjection(false)
                .isMine(loginedUser.equals(user))
                .build();
    }

    private static Long getObjectId(Objection objection) {
        if (objection == null) {
            return null;
        }
        return objection.getId();
    }

    public static WorkoutConfirmationOrObjectionResponse objection(User loginedUser, Objection objection, User objectionTargetUser) {
        LocalDateTime createdAt = objection.getCreatedAt();
        return WorkoutConfirmationOrObjectionResponse.builder()
                .objectionId(objection.getId())
                .workoutConfirmationId(objection.getWorkoutConfirmation().getId())
                .nickname(objectionTargetUser.getNickname())
                .profileImageUrl(objectionTargetUser.getProfileImageName())
                .workoutConfirmationImageUrl(null)
                .createdAt(createdAt)
                .dayOfTheWeek(createdAt.getDayOfWeek().name())
                .isObjection(true)
                .isMine(loginedUser.equals(objectionTargetUser))
                .build();
    }
}
