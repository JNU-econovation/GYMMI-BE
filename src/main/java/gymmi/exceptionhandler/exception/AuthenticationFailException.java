package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionType;

public class AuthenticationFailException extends NotMatchedException {

    public static final ExceptionType EXCEPTION_CODE = ExceptionType.AUTHENTICATION_FAIL;

    public AuthenticationFailException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    protected AuthenticationFailException(ErrorCode errorCode, ExceptionType exceptionType) {
        super(errorCode, exceptionType);
    }

    public AuthenticationFailException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }

    protected AuthenticationFailException(ErrorCode errorCode, ExceptionType exceptionType, Throwable throwable) {
        super(errorCode, exceptionType, throwable);
    }
}
