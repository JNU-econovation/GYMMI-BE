package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.exceptionhandler.message.ExceptionType;

public class InvalidFileException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_FILE;

    public InvalidFileException(ErrorMessage errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
