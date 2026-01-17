package gymmi.global.exception.exceptiontype;

import gymmi.global.exception.message.ErrorCode;
import gymmi.global.exception.message.ExceptionType;

public class UnsupportedException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.UNSUPPORTED;

    public UnsupportedException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
