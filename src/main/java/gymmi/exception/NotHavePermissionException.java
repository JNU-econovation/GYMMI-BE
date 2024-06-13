package gymmi.exception;

public class NotHavePermissionException extends BadRequestException {

    public static final String ERROR_CODE = "NOT_HAVE_PERMISSION";
    public static final String ERROR_DESCRIPTION = "권한이 없는 경우";

    public NotHavePermissionException(String message) {
        super(message, ERROR_CODE, ERROR_DESCRIPTION);
    }
}
