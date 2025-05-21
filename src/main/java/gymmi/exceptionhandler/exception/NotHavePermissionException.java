package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class NotHavePermissionException extends GymmiException {
    public static final String COMMENT = "권한이 없는 경우";

    public NotHavePermissionException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public NotHavePermissionException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
