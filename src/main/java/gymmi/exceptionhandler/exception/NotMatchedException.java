package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class NotMatchedException extends GymmiException {

    public static final String COMMENT = "일치하지 않는 경우";

    public NotMatchedException() {
    }

    public NotMatchedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public NotMatchedException(String message) {
        super(message);
    }

    public NotMatchedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMatchedException(Throwable cause) {
        super(cause);
    }
}
