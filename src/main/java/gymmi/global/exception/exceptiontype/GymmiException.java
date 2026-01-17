package gymmi.global.exception.exceptiontype;

import gymmi.global.exception.message.ErrorCode;
import gymmi.global.exception.message.ExceptionType;

public class GymmiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final ExceptionType exceptionType;

    protected GymmiException(ErrorCode errorCode, ExceptionType exceptionType) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.exceptionType = exceptionType;
    }

    protected GymmiException(ErrorCode errorCode, ExceptionType exceptionType, Throwable throwable) {
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
