package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionCode;

public class AlreadyExistException extends GymmiException {

    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.ALREADY_EXISTS;

    public AlreadyExistException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
