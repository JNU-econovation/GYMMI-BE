package gymmi.exception;

public class AuthenticationException extends BadRequestException {

    public static final String ERROR_CODE = "AUTHENTICATION";
    public static final String ERROR_DESCRIPTION = "인증에 실패한 경우";

    public AuthenticationException(String message) {
        super(message, ERROR_CODE, ERROR_DESCRIPTION);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE, ERROR_DESCRIPTION);
    }
}
