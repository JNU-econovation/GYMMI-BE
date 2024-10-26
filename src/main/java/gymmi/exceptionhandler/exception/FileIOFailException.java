package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionCode;

public class FileIOFailException extends FileException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.FILE_IO_FAIL;

    public FileIOFailException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    public FileIOFailException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }
}
