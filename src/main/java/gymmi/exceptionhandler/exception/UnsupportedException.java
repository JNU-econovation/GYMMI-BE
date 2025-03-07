package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionType;

public class UnsupportedException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.UNSUPPORTED;

    public UnsupportedException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
