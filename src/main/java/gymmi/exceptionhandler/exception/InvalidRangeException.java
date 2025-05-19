package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.exceptionhandler.message.ExceptionType;

public class InvalidRangeException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_RANGE;

    public InvalidRangeException(ErrorMessage errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
