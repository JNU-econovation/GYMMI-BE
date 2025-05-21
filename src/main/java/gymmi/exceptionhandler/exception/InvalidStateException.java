package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class InvalidStateException extends GymmiException {
    public static final String COMMENT = "조건에 맞지 않는 상태인 경우";

    public InvalidStateException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidStateException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
