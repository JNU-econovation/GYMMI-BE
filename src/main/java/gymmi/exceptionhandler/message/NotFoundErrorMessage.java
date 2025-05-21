package gymmi.exceptionhandler.message;

public enum NotFoundErrorMessage implements ErrorMessage {

    NOT_FOUND_MISSION("해당 미션이 존재하지 않습니다.", 404),
    NOT_FOUND_WORKOUT_CONFIRMATION("해당 운동 기록이 존재하지 않아요.", 404),
    NOT_FOUND_WORKSPACE("해당 워크스페이스가 존재하지 않아요.", 404),
    NOT_FOUND_WORKSPACE_RESULT("해당 워크스페이스 결과가 존재하지 않아요.", 404),
    NOT_FOUND_WORKER("워커가 존재하지 않아요", 404),
    NOT_FOUND_OBJECTION("해당 이의 신청이 존재하지 않습니다.", 404),
    NOT_FOUND_USER("유저 정보를 찾을 수 없습니다.", 404),
    NOT_FOUND_PROFILE_IMAGE("프로필 이미지 찾을 수 없습니다.", 400),
    NOT_FOUND_PHOTO_FEED("존재하지 않는 사진피드 입니다.", 400),
    NOT_FOUND_PHOTO_FEED_IMAGE("존재하지 않는 사진 입니다.", 400),
    NOT_FOUND_FILE("해당 파일이 존재하지 않습니다.", 404),
    NOT_FOUND_IMAGE_OBJECT("존재하지 않은 이미지 object 입니다.", 400),
    ;

    private final String message;
    private final int statusCode;

    NotFoundErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
