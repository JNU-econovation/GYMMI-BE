package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class InvalidNumberException extends InvalidStateException {
    public static final String COMMENT = "조건에 맞지 않는 숫자인 경우";

    public InvalidNumberException() {
    }

    public InvalidNumberException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidNumberException(String message) {
        super(message);
    }

    public InvalidNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNumberException(Throwable cause) {
        super(cause);
    }
}
