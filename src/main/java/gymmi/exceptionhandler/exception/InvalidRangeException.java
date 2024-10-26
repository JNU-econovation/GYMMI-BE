package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionCode;

public class InvalidRangeException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INVALID_RANGE;

    public InvalidRangeException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
