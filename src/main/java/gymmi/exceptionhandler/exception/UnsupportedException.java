package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.exceptionhandler.message.ExceptionType;

public class UnsupportedException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.UNSUPPORTED;

    public UnsupportedException(ErrorMessage errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
