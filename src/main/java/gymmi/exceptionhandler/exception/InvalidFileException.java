package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionCode;

public class InvalidFileException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INVALID_FILE;

    public InvalidFileException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
