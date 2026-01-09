package gymmi.global.exceptionhandler.exception;

import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.global.exceptionhandler.message.ExceptionType;

public class InvalidPatternException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_PATTERN;

    public InvalidPatternException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
