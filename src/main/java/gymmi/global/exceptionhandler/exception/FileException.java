package gymmi.global.exceptionhandler.exception;

import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.global.exceptionhandler.message.ExceptionType;

public class FileException extends GymmiException {

    public static final ExceptionType EXCEPTION_CODE = ExceptionType.FILE_RELATED;

    public FileException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    public FileException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }

    protected FileException(ErrorCode errorCode, ExceptionType exceptionType) {
        super(errorCode, exceptionType);
    }

    protected FileException(ErrorCode errorCode, ExceptionType exceptionType, Throwable throwable) {
        super(errorCode, exceptionType, throwable);
    }

}
