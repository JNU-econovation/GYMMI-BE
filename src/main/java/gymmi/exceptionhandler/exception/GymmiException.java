package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.message.ErrorMessage;

public class GymmiException extends RuntimeException {

    public static final String COMMENT = "지미 최상위 예외 클래스";


    public GymmiException() {
    }

    public GymmiException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }

    public GymmiException(String message) {
        super(message);
    }

    public GymmiException(String message, Throwable cause) {
        super(message, cause);
    }

    public GymmiException(Throwable cause) {
        super(cause);
    }

}
