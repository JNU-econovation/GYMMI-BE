package gymmi.exceptionhandler.message;

public enum WorkspaceEditErrorMessage implements ErrorMessage {

    // 워크스페이스 수정
    ALREADY_ACTIVATED_WORKSPACE("이미 진행중이거나 종료된 워크스페이스 입니다.", 400),
    NOT_WORKSPACE_CREATOR("해당 워크스페이스의 방장이 아닙니다.", 403),
    ;

    private final String message;
    private final int statusCode;

    WorkspaceEditErrorMessage(String message, int statusCode) {
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
