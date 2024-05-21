package gymmi.exception;

public class GymmiException extends RuntimeException {

    private final String errorCode;
    private final String errorDescription;


    public GymmiException(String message, String errorCode, String errorDescription) {
        super(message);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
