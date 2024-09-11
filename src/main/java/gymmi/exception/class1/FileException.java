package gymmi.exception.class1;

import gymmi.exception.message.ErrorCode;
import gymmi.exception.message.ExceptionCode;

public class FileException extends GymmiException {

    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.FILE_RELATED;

    public FileException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    public FileException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }

    protected FileException(ErrorCode errorCode, ExceptionCode exceptionCode) {
        super(errorCode, exceptionCode);
    }

    protected FileException(ErrorCode errorCode, ExceptionCode exceptionCode, Throwable throwable) {
        super(errorCode, exceptionCode, throwable);
    }

}
