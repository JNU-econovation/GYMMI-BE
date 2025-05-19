package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.exceptionhandler.message.ExceptionType;

public class NotFoundException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.NOT_FOUND;

    public NotFoundException(ErrorMessage errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
