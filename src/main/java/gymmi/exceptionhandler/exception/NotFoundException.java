package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionCode;

public class NotFoundException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.NOT_FOUND;

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
