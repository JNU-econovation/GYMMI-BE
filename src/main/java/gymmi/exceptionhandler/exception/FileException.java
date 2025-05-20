package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class FileException extends GymmiException {

    public static final String COMMENT = "파일과 관련된 경우";

    public FileException() {
    }

    public FileException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }
}
