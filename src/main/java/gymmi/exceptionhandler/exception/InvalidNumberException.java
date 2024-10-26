package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionCode;

public class InvalidNumberException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INVALID_NUMBER;

    public InvalidNumberException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
