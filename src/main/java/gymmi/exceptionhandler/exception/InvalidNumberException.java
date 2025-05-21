package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class InvalidNumberException extends InvalidStateException {
    public static final String COMMENT = "조건에 맞지 않는 숫자인 경우";

    public InvalidNumberException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidNumberException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
