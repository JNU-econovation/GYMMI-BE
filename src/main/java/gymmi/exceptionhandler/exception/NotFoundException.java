package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class NotFoundException extends GymmiException {

    private static final String COMMENT = "찾고자하는 것이 존재 하지 않는 경우";

    public NotFoundException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
