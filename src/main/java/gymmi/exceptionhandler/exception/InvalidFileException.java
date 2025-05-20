package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class InvalidFileException extends FileException {
    public static final String COMMENT = "조건에 맞지 않는 파일인 경우";

    public InvalidFileException() {
    }

    public InvalidFileException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidFileException(String message) {
        super(message);
    }

    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFileException(Throwable cause) {
        super(cause);
    }
}
