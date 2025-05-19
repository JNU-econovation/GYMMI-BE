package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.exceptionhandler.message.ExceptionType;

public class GymmiException extends RuntimeException {

    private final ErrorMessage errorCode;
    private final ExceptionType exceptionType;

    protected GymmiException(ErrorMessage errorCode, ExceptionType exceptionType) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.exceptionType = exceptionType;
    }

    protected GymmiException(ErrorMessage errorCode, ExceptionType exceptionType, Throwable throwable) {
        super(errorCode.getMessage(), throwable);
        this.errorCode = errorCode;
        this.exceptionType = exceptionType;
    }

    public int getStatusCode() {
        return errorCode.getStatusCode();
    }

    public ExceptionType getExceptionCode() {
        return exceptionType;
    }
}
