package gymmi.global.exception.exceptiontype;

import gymmi.global.exception.message.ErrorCode;
import gymmi.global.exception.message.ExceptionType;

public class NotFoundException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.NOT_FOUND;

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
