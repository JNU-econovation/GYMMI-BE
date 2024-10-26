package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionType;

public class NotMatchedException extends GymmiException {

    public static final ExceptionType EXCEPTION_CODE = ExceptionType.NOT_MATCHED;

    public NotMatchedException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    protected NotMatchedException(ErrorCode errorCode, ExceptionType exceptionType) {
        super(errorCode, exceptionType);
    }

    public NotMatchedException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }

    protected NotMatchedException(ErrorCode errorCode, ExceptionType exceptionType, Throwable throwable) {
        super(errorCode, exceptionType, throwable);
    }
}
