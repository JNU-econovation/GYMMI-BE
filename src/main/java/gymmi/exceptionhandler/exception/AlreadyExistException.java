package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class AlreadyExistException extends GymmiException {

    public static final String COMMENT = "이미 존재하는 경우";

    public AlreadyExistException() {
    }

    public AlreadyExistException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public AlreadyExistException(String message) {
        super(message);
    }

    public AlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistException(Throwable cause) {
        super(cause);
    }
}
