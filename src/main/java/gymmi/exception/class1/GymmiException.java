package gymmi.exception.class1;

import gymmi.exception.message.ErrorCode;
import gymmi.exception.message.ExceptionCode;

public class GymmiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final ExceptionCode exceptionCode;

    protected GymmiException(ErrorCode errorCode, ExceptionCode exceptionCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.exceptionCode = exceptionCode;
    }

    protected GymmiException(ErrorCode errorCode, ExceptionCode exceptionCode, Throwable throwable) {
        super(errorCode.getMessage(), throwable);
        this.errorCode = errorCode;
        this.exceptionCode = exceptionCode;
    }

    public int getStatusCode() {
        return errorCode.getStatusCode();
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
