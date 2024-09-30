package gymmi.exception.class1;

import gymmi.exception.message.ErrorCode;
import gymmi.exception.message.ExceptionCode;

public class NotMatchedException extends GymmiException {

    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.NOT_MATCHED;

    public NotMatchedException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    protected NotMatchedException(ErrorCode errorCode, ExceptionCode exceptionCode) {
        super(errorCode, exceptionCode);
    }

    public NotMatchedException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }

    protected NotMatchedException(ErrorCode errorCode, ExceptionCode exceptionCode, Throwable throwable) {
        super(errorCode, exceptionCode, throwable);
    }
}
