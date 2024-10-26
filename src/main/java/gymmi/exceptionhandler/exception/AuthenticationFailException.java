package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionCode;

public class AuthenticationFailException extends NotMatchedException {

    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.AUTHENTICATION_FAIL;

    public AuthenticationFailException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    protected AuthenticationFailException(ErrorCode errorCode, ExceptionCode exceptionCode) {
        super(errorCode, exceptionCode);
    }

    public AuthenticationFailException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }

    protected AuthenticationFailException(ErrorCode errorCode, ExceptionCode exceptionCode, Throwable throwable) {
        super(errorCode, exceptionCode, throwable);
    }
}
