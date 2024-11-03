package gymmi.exceptionhandler.legacy;

public class BadRequestException extends GymmiException {

    public static final String ERROR_CODE = "INVALID_REQUEST";
    public static final String ERROR_DESCRIPTION = "요청이 잘못 된 경우";

    public BadRequestException(String message, String errorCode, String errorDescription) {
        super(message, errorCode, errorDescription);
    }

    public BadRequestException(String message, Throwable cause, String errorCode, String errorDescription) {
        super(message, cause, errorCode, errorDescription);
    }

    public BadRequestException(String message, Throwable cause) {
        this(message, cause, ERROR_CODE, ERROR_DESCRIPTION);
    }

    public BadRequestException(String message) {
        this(message, ERROR_CODE, ERROR_DESCRIPTION);
    }

    @Override
    public String getErrorTitle() {
        return super.getErrorTitle();
    }

    @Override
    public String getErrorDescription() {
        return super.getErrorDescription();
    }
}
