package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class FileIOFailException extends FileException {
    public static final String COMMENT = "파일 입출력과 관련된 경우";

    public FileIOFailException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public FileIOFailException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
