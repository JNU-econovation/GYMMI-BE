package gymmi.exceptionhandler;

import gymmi.exceptionhandler.exception.GymmiException;
import lombok.Getter;

@Getter
public class ErrorResponse1 {

    private final ErrorCode errorCode;
    private final String description;
    private final String errorMessage;

    public ErrorResponse1(ErrorCode errorCode, String description, String errorMessage) {
        this.errorCode = errorCode;
        this.description = description;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse1(GymmiException e) {
        this.errorCode = e.getErrorCode();
        this.description = e.getErrorCode().getValue();
        this.errorMessage = e.getMessage();
    }
}

