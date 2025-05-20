package gymmi.exceptionhandler.message;

public enum WorkspaceJoinErrorMessage implements ErrorMessage {

    ALREADY_JOINED_WORKSPACE("이미 참여한 워크스페이스 입니다.", 400),
    FULL_WORKSPACE("워크스페이스 인원이 가득 찼습니다.", 400),
    ALREADY_ACTIVATED_WORKSPACE("이미 진행중이거나 종료된 워크스페이스 입니다.", 400),
    EXCEED_MAX_JOINED_WORKSPACE("워크스페이스는 5개까지 참여 가능합니다.(완료된 워크스페이스 제외)", 400),
    NOT_MATCHED_PASSWORD("비밀번호가 일치하지 않습니다.", 400),
    ;


    private final String message;
    private final int statusCode;

    WorkspaceJoinErrorMessage(String message, int statusCode) {
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
