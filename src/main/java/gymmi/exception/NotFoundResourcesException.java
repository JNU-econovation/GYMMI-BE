package gymmi.exception;

public class NotFoundResourcesException extends NotFoundException {

    public static final String ERROR_CODE = "NOT_FOUND";
    public static final String ERROR_DESCRIPTION = "찾고자하는 것이 존재 하지 않는 경우";

    public NotFoundResourcesException(String message) {
        super(message, ERROR_CODE, ERROR_DESCRIPTION);
    }
}
