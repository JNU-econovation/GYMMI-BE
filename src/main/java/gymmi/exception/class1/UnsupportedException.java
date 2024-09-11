package gymmi.exception.class1;

import gymmi.exception.message.ErrorCode;
import gymmi.exception.message.ExceptionCode;

public class UnsupportedException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.UNSUPPORTED;

    public UnsupportedException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
