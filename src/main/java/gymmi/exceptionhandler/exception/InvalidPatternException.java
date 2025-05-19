package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.exceptionhandler.message.ExceptionType;

public class InvalidPatternException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.INVALID_PATTERN;

    public InvalidPatternException(ErrorMessage errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
