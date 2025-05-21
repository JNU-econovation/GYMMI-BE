package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class FileException extends GymmiException {

    public static final String COMMENT = "파일과 관련된 경우";

    public FileException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public FileException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
