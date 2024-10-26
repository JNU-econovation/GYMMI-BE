package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionType;

public class InvalidRangeException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_RANGE;

    public InvalidRangeException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
