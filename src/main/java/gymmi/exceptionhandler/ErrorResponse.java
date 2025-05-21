package gymmi.exceptionhandler;

import gymmi.exceptionhandler.exception.GymmiException;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final ErrorCode errorCode;
    private final String description;
    private final String errorMessage;

    public ErrorResponse(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.description = errorCode.getValue();
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(GymmiException e) {
        this(e.getErrorCode(), e.getMessage());
    }
}

