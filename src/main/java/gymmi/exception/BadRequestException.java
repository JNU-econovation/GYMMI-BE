package gymmi.exception;

public class BadRequestException extends GymmiException {

    public BadRequestException(String message, String errorCode, String errorDescription) {
        super(message, errorCode, errorDescription);
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
