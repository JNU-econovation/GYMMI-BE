package gymmi.exceptionhandler.message;

public enum LoginErrorMessage implements ErrorMessage {

    MISSING_AUTHORIZATION_HEADER("인증 헤더의 값이 비어있습니다.", 401),
    UNSUPPORTED_AUTHORIZATION_TYPE("지원하지 않는 인증 방식 입니다. Bearer 타입으로 인증해 주세요.", 401),
    UNUSUAL_AUTHORIZATION_ACCESS("비정상적인 접근입니다. 다시 로그인 해주세요.", 401),
    FAILED_LOGIN("아이디와 비밀번호를 확인해 주세요.", 401),
    NOT_MATCHED_PASSWORD("비밀번호가 일치하지 않습니다.", 400),
    NOT_MATCHED_JWT_SUBJECT("토큰 제목을 확인해 주세요.", 401),
    EXPIRED_JWT("토큰이 만료되었습니다.", 401),
    JWT_RELATED_ERROR("토큰 관련 에러 발생.", 500);

    private final String message;
    private final int statusCode;

    LoginErrorMessage(String message, int statusCode) {
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
