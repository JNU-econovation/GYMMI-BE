package gymmi.exception;

public class NotMatchedException extends BadRequestException {

    public static final String ERROR_CODE = "NOT_MATCHED";
    public static final String ERROR_DESCRIPTION = "(어떤 것 이든) 일치 하지 않는 경우";

    public NotMatchedException(String message) {
        super(message, ERROR_CODE, ERROR_DESCRIPTION);
    }
}
