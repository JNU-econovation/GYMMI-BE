package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionCode;

public class InvalidStateException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INVALID_STATE;

    public InvalidStateException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
