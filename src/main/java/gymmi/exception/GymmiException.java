package gymmi.exception;

public class GymmiException extends RuntimeException {

    private final String errorTitle;
    private final String errorDescription;


    public GymmiException(String message, String errorTitle, String errorDescription) {
        super(message);
        this.errorTitle = errorTitle;
        this.errorDescription = errorDescription;
    }

    public GymmiException(String message, Throwable cause, String errorTitle, String errorDescription) {
        super(message, cause);
        this.errorTitle = errorTitle;
        this.errorDescription = errorDescription;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
