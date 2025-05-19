package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;
import gymmi.exceptionhandler.message.ExceptionType;

public class FileException extends GymmiException {

    public static final ExceptionType EXCEPTION_CODE = ExceptionType.FILE_RELATED;

    public FileException(ErrorMessage errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    public FileException(ErrorMessage errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }

    protected FileException(ErrorMessage errorCode, ExceptionType exceptionType) {
        super(errorCode, exceptionType);
    }

    protected FileException(ErrorMessage errorCode, ExceptionType exceptionType, Throwable throwable) {
        super(errorCode, exceptionType, throwable);
    }

}
