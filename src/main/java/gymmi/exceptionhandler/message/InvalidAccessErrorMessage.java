package gymmi.exceptionhandler.message;

public enum InvalidAccessErrorMessage implements ErrorMessage {

    NO_WORKOUT_HISTORY_EXIST_IN_WORKSPACE("해당 워크스페이스의 운동 기록이 아니에요", 403),
    NO_OBJECTION_EXIST_IN_WORKSPACE("해당 워크스페이스와 관련 정보가 아니에요", 403),
    NOT_JOINED_WORKSPACE("해당 워크스페이스의 참여자가 아니에요.", 403);

    private final String message;
    private final int statusCode;

    InvalidAccessErrorMessage(String message, int statusCode) {
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
