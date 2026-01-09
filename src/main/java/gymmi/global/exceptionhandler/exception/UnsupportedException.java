package gymmi.global.exceptionhandler.exception;

import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.global.exceptionhandler.message.ExceptionType;

public class UnsupportedException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.UNSUPPORTED;

    public UnsupportedException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
