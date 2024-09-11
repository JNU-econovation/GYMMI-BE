package gymmi.exception.class1;

import gymmi.exception.message.ErrorCode;
import gymmi.exception.message.ExceptionCode;

public class NotHavePermissionException extends GymmiException {
    public static final ExceptionCode EXCEPTION_CODE = ExceptionCode.NOT_HAVE_PERMISSION;

    public NotHavePermissionException(ErrorCode errorCode) {
        super(errorCode, EXCEPTION_CODE);
    }
}
