package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.exceptionhandler.message.ExceptionType;

public class NotMatchedException extends GymmiException {

    public static final ExceptionType EXCEPTION_CODE = ExceptionType.NOT_MATCHED;

    public NotMatchedException(ErrorMessage errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    protected NotMatchedException(ErrorMessage errorCode, ExceptionType exceptionType) {
        super(errorCode, exceptionType);
    }

    public NotMatchedException(ErrorMessage errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }

    protected NotMatchedException(ErrorMessage errorCode, ExceptionType exceptionType, Throwable throwable) {
        super(errorCode, exceptionType, throwable);
    }
}
