package gymmi.exceptionhandler.message;

public enum ServerErrorMessage implements ErrorMessage {
    RETRY_AFTER_LOGOUT("로그아웃 후 다시 시도해 주세요", 500),
    LOGIC_ERROR("벡엔드 문의 바람", 500),
    JWT_RELATED_ERROR("토큰 관련 에러 발생.", 500),
    FAILED_FILE_UPLOAD("파일 업로드를 실패하였습니다.", 500),
    FAILED_FILE_DELETION("파일 삭제를 실패하였습니다.", 500),
    ;

    private final String message;
    private final int statusCode;

    ServerErrorMessage(String message, int statusCode) {
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
