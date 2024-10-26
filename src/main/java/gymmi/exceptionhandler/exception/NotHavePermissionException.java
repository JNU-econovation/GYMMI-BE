package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionType;

public class NotHavePermissionException extends GymmiException {
    public static final ExceptionType EXCEPTION_CODE = ExceptionType.NOT_HAVE_PERMISSION;

    public NotHavePermissionException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
