package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.exceptionhandler.message.ExceptionType;

public class InvalidNumberException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_NUMBER;

    public InvalidNumberException(ErrorMessage errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
