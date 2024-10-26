package gymmi.exceptionhandler.legacy;

public class NotFoundException extends GymmiException {

    public NotFoundException(String message, String errorCode, String errorDescription) {
        super(message, errorCode, errorDescription);
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
