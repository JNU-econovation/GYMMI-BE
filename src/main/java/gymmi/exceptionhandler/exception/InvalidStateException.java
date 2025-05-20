package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class InvalidStateException extends GymmiException {
    public static final String COMMENT = "조건에 맞지 않는 상태인 경우";

    public InvalidStateException() {
    }

    public InvalidStateException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidStateException(String message) {
        super(message);
    }

    public InvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidStateException(Throwable cause) {
        super(cause);
    }
}
