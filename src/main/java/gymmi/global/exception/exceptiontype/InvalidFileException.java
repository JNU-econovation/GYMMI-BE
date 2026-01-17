package gymmi.global.exception.exceptiontype;

import gymmi.global.exception.message.ErrorCode;
import gymmi.global.exception.message.ExceptionType;

public class InvalidFileException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_FILE;

    public InvalidFileException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
