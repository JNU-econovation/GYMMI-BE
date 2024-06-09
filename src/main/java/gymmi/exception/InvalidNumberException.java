package gymmi.exception;

public class InvalidNumberException extends BadRequestException {

    public static final String ERROR_CODE = "INVALID_NUMBER";
    public static final String ERROR_DESCRIPTION = "조건에 맞지 않는 숫자인 경우";

    public InvalidNumberException(String message) {
        super(message, ERROR_CODE, ERROR_DESCRIPTION);
    }
}
