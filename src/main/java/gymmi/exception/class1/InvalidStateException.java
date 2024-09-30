package gymmi.exception.class1;

import gymmi.exception.message.ErrorCode;
import gymmi.exception.message.ExceptionCode;

public class InvalidStateException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INVALID_STATE;

    public InvalidStateException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
