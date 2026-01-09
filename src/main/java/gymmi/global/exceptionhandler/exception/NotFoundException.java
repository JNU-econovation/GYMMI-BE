package gymmi.global.exceptionhandler.exception;

import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.global.exceptionhandler.message.ExceptionType;

public class NotFoundException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.NOT_FOUND;

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
