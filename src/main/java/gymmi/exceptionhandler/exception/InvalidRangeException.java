package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class InvalidRangeException extends InvalidStateException {
    public static final String COMMENT = "조건에 맞지 않는 범위인 경우";

    public InvalidRangeException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidRangeException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
