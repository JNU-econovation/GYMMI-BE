package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionType;

public class InvalidFileException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_FILE;

    public InvalidFileException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
