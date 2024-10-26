package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionCode;

public class InvalidPatternException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INVALID_PATTERN;

    public InvalidPatternException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
