package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionType;

public class InvalidPatternException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_PATTERN;

    public InvalidPatternException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
