package gymmi.global.exceptionhandler.exception;

import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.global.exceptionhandler.message.ExceptionType;

public class InvalidNumberException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_NUMBER;

    public InvalidNumberException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
