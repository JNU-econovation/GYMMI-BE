package gymmi.exception;

public class InvalidPatternException extends BadRequestException {

    public static final String ERROR_CODE = "INVALID_PATTERN";
    public static final String ERROR_DESCRIPTION = "조건에 맞지 않는 문자열인 경우";

    public InvalidPatternException(String message) {
        super(message, ERROR_CODE, ERROR_DESCRIPTION);
    }
}
