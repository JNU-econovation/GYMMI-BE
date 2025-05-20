package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class AuthenticationFailException extends NotMatchedException {

    public static final String COMMENT = "인증에 실패한 경우";

    public AuthenticationFailException() {
    }

    public AuthenticationFailException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public AuthenticationFailException(String message) {
        super(message);
    }

    public AuthenticationFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationFailException(Throwable cause) {
        super(cause);
    }
}
