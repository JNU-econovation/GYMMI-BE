package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionType;

public class NotFoundException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.NOT_FOUND;

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
