package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class UnsupportedException extends GymmiException {
    public static final String COMMENT = "지원하지 않는 경우";

    public UnsupportedException() {
    }

    public UnsupportedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public UnsupportedException(String message) {
        super(message);
    }

    public UnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedException(Throwable cause) {
        super(cause);
    }
}
