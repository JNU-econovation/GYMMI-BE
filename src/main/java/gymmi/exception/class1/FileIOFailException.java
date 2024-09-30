package gymmi.exception.class1;

import gymmi.exception.message.ErrorCode;
import gymmi.exception.message.ExceptionCode;

public class FileIOFailException extends FileException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.FILE_IO_FAIL;

    public FileIOFailException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }

    public FileIOFailException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, EXCEPTION_CODE, throwable);
    }
}
