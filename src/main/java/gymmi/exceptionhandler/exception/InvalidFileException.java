package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class InvalidFileException extends FileException {
    public static final String COMMENT = "조건에 맞지 않는 파일인 경우";

    public InvalidFileException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidFileException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
