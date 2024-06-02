package gymmi.exception;

public class NotFoundException extends GymmiException {

    public NotFoundException(String message, String errorCode, String errorDescription) {
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
