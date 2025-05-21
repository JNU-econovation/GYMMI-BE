package gymmi.exceptionhandler.message;

public enum WorkspaceConsistencyErrorMessage implements ErrorMessage {

    NOT_CONSISTENT_WORKERS_COUNT("워크스페이스 인원이 잘못 되었습니다.", 500),
    NOT_CONSISTENT_MISSIONS_COUNT("미션 수가 잘못 되었습니다.", 500),
    EXIST_NOT_JOINED_WORKER("해당 워크스페이스의 참여자가 아닌 사람이 존재합니다.", 500),
    EXIST_NOT_REGISTERED_MISSION("워크스페이스에 등록 되지 않은 미션이 존재합니다.", 500),
    ;

    private final String message;
    private final int statusCode;

    WorkspaceConsistencyErrorMessage(String message, int statusCode) {
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
