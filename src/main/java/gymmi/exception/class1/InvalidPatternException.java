package gymmi.exception.class1;

import gymmi.exception.message.ErrorCode;
import gymmi.exception.message.ExceptionCode;

public class InvalidPatternException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INVALID_NUMBER;

    public InvalidPatternException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
