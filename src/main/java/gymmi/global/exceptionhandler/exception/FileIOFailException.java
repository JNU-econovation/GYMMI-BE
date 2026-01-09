package gymmi.global.exceptionhandler.exception;

import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.global.exceptionhandler.message.ExceptionType;

public class FileIOFailException extends FileException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.FILE_IO_FAIL;

    public FileIOFailException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    public FileIOFailException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }
}
