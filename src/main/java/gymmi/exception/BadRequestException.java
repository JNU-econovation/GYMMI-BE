package gymmi.exception;

public class BadRequestException extends GymmiException {

    public static final String ERROR_CODE = "INVALID_REQUEST";
    public static final String ERROR_DESCRIPTION = "요청이 잘못 된 경우";

    public BadRequestException(String message, String errorCode, String errorDescription) {
        super(message, errorCode, errorDescription);
    }

    public BadRequestException(String message) {
        this(message, ERROR_CODE, ERROR_DESCRIPTION);
    }

    @Override
    public String getErrorCode() {
        return super.getErrorCode();
    }

    @Override
    public String getErrorDescription() {
        return super.getErrorDescription();
    }
}
