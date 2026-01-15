package gymmi.global.exception.exceptiontype;

import gymmi.global.exception.message.ErrorCode;
import gymmi.global.exception.message.ExceptionType;

public class InvalidPatternException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_PATTERN;

    public InvalidPatternException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
