package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionType;

public class InvalidStateException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_STATE;

    public InvalidStateException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
