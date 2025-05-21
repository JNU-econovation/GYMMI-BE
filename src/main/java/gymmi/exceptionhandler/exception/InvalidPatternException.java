package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class InvalidPatternException extends InvalidStateException {
    public static final String COMMENT = "조건에 맞지 않는 문자열인 경우";

    public InvalidPatternException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public InvalidPatternException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
