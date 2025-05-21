package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class AuthenticationFailException extends NotMatchedException {

    public static final String COMMENT = "인증에 실패한 경우";

    public AuthenticationFailException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public AuthenticationFailException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
