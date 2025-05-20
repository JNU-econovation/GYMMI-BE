package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class NotHavePermissionException extends GymmiException {
    public static final String COMMENT = "권한이 없는 경우";

    public NotHavePermissionException() {
    }

    public NotHavePermissionException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public NotHavePermissionException(String message) {
        super(message);
    }

    public NotHavePermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotHavePermissionException(Throwable cause) {
        super(cause);
    }
}
