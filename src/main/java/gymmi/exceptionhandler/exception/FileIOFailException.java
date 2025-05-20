package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class FileIOFailException extends FileException {
    public static final String COMMENT = "파일 입출력과 관련된 경우";

    public FileIOFailException() {
    }

    public FileIOFailException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public FileIOFailException(String message) {
        super(message);
    }

    public FileIOFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileIOFailException(Throwable cause) {
        super(cause);
    }
}
