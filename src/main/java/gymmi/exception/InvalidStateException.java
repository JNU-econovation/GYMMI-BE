package gymmi.exception;

public class InvalidStateException extends BadRequestException {

    public static final String ERROR_CODE = "INVALID_STATE";
    public static final String ERROR_DESCRIPTION = "조건에 맞지 않는 상태인 경우";

    public InvalidStateException(String message) {
        super(message, ERROR_CODE, ERROR_DESCRIPTION);
    }
}
