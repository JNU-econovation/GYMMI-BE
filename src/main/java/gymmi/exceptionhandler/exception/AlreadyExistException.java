package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.exceptionhandler.message.ExceptionType;

public class AlreadyExistException extends GymmiException {

    public static final ExceptionType EXCEPTION_CODE = ExceptionType.ALREADY_EXISTS;

    public AlreadyExistException(ErrorMessage errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
