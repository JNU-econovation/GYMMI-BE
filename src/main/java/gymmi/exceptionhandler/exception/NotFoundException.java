package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class NotFoundException extends GymmiException {

    private static final String COMMENT = "찾고자하는 것이 존재 하지 않는 경우";

    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

}
