package gymmi.global.exception.exceptiontype;

import gymmi.global.exception.message.ErrorCode;
import gymmi.global.exception.message.ExceptionType;

public class AlreadyExistException extends GymmiException {

    public static final ExceptionType EXCEPTION_CODE = ExceptionType.ALREADY_EXISTS;

    public AlreadyExistException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
