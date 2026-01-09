package gymmi.global.exceptionhandler.exception;

import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.global.exceptionhandler.message.ExceptionType;

public class AlreadyExistException extends GymmiException {

    public static final ExceptionType EXCEPTION_CODE = ExceptionType.ALREADY_EXISTS;

    public AlreadyExistException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
