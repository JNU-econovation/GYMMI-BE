package gymmi.global.exceptionhandler.exception;

import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.global.exceptionhandler.message.ExceptionType;

public class InvalidStateException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_STATE;

    public InvalidStateException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
