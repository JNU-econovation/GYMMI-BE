package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.ExceptionCode;

public class NotHavePermissionException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.NOT_HAVE_PERMISSION;

    public NotHavePermissionException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
