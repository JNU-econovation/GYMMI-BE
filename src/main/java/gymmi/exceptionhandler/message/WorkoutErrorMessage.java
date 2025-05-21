package gymmi.exceptionhandler.message;

public enum WorkoutErrorMessage implements ErrorMessage {

    INACTIVE_WORKSPACE("워크스페이스가 진행중이 아니에요.", 400),
    EXCEED_MAX_DAILY_WORKOUT_HISTORY_COUNT("일일 가능한 운동을 초과했어요! (3회까지 가능)", 400),
    NOT_REGISTERED_WORKSPACE_MISSION("워크스페이스에 등록된 미션이 아닙니다.", 400),
    NOT_JOINED_WORKSPACE("해당 워크스페이스의 참여자가 아니에요.", 403),

    ;
    private final String message;
    private final int statusCode;

    WorkoutErrorMessage(String message, int statusCode) {
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
