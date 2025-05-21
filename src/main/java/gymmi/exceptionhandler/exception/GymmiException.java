package gymmi.exceptionhandler.exception;

import gymmi.exceptionhandler.errrorcode.ErrorCode;
import gymmi.exceptionhandler.errrorcode.Mapper;
import gymmi.exceptionhandler.message.ErrorContext;
import gymmi.exceptionhandler.message.ErrorMessage;

public class GymmiException extends RuntimeException implements ErrorContext {

    public static final String COMMENT = "지미 최상위 예외 클래스";
    private final ErrorMessage errorMessage;


    public GymmiException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public GymmiException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage.getMessage(), cause);
        this.errorMessage = errorMessage;
    }

    @Override
    public ErrorCode getErrorCode() {
        return Mapper.getErrorCode(errorMessage);
    }
}
