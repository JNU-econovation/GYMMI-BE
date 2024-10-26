package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionCode;

public class UnsupportedException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.UNSUPPORTED;

    public UnsupportedException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
