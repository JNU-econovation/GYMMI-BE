package gymmi.exception;

public class PreparingException extends GymmiException {

    public static final String ERROR_CODE = "PREPARING";
    public static final String ERROR_DESCRIPTION = "아직 준비중인 기능인 경우";

    public PreparingException(String message) {
        super(message, ERROR_CODE, ERROR_DESCRIPTION);
    }
}
