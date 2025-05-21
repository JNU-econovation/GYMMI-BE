package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class AlreadyExistException extends GymmiException {

    public static final String COMMENT = "이미 존재하는 경우";

    public AlreadyExistException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public AlreadyExistException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
