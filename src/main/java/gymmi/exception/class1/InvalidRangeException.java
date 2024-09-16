package gymmi.exception.class1;

import gymmi.exception.message.ErrorCode;
import gymmi.exception.message.ExceptionCode;

public class InvalidRangeException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INVALID_RANGE;

    public InvalidRangeException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
