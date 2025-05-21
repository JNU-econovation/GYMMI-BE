package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class NotMatchedException extends GymmiException {

    public static final String COMMENT = "일치하지 않는 경우";

    public NotMatchedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public NotMatchedException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
