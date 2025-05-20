package gymmi.exceptionhandler.message;

public enum RepositoryErrorMessage implements ErrorMessage {

    NOT_FOUND_MISSION("해당 미션이 존재하지 않습니다.", 404),
    NOT_FOUND_WORKOUT_CONFIRMATION("해당 운동 기록이 존재하지 않아요.", 404),
    NOT_FOUND_WORKSPACE("해당 워크스페이스가 존재하지 않아요.", 404),
    NOT_FOUND_WORKSPACE_RESULT("해당 워크스페이스 결과가 존재하지 않아요.", 404),
    NOT_FOUND_WORKER("워커가 존재하지 않아요", 404),
    NOT_FOUND_OBJECTION("해당 이의 신청이 존재하지 않습니다.", 404);

    private final String message;
    private final int statusCode;

    RepositoryErrorMessage(String message, int statusCode) {
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
