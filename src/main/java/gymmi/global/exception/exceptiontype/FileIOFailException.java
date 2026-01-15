package gymmi.global.exception.exceptiontype;

import gymmi.global.exception.message.ErrorCode;
import gymmi.global.exception.message.ExceptionType;

public class FileIOFailException extends FileException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.FILE_IO_FAIL;

    public FileIOFailException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    public FileIOFailException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }
}
