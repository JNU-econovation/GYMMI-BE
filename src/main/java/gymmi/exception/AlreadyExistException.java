package gymmi.exception;

public class AlreadyExistException extends BadRequestException {

    public static final String ERROR_CODE = "ALREADY_EXIST";
    public static final String ERROR_DESCRIPTION = "(어떠한 것이든) 이미 존재하는 경우";

    public AlreadyExistException(String message) {
        super(message, ERROR_CODE, ERROR_DESCRIPTION);
    }
}
