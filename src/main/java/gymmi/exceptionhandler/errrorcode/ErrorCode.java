package gymmi.exceptionhandler.errrorcode;

public enum ErrorCode {

    AUTH_FAIL("사용자 인증에 실패한 경우", 401),
    INVALID_("", 400),
    SERVER_ERROR("확인되지 않은 에러인경우. 백엔드에 문의해주세요", 500);

    private final String value;
    private final int statusCode;

    ErrorCode(String value, int statusCode) {
        this.value = value;
        this.statusCode = statusCode;
    }
}
