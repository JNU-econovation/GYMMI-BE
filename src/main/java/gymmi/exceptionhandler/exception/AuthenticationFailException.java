package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.exceptionhandler.message.ExceptionType;

public class AuthenticationFailException extends NotMatchedException {

    public static final ExceptionType EXCEPTION_CODE = ExceptionType.AUTHENTICATION_FAIL;

    public AuthenticationFailException(ErrorMessage errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    protected AuthenticationFailException(ErrorMessage errorCode, ExceptionType exceptionType) {
        super(errorCode, exceptionType);
    }

    public AuthenticationFailException(ErrorMessage errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }

    protected AuthenticationFailException(ErrorMessage errorCode, ExceptionType exceptionType, Throwable throwable) {
        super(errorCode, exceptionType, throwable);
    }
}
