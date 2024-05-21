package gymmi.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String errorCode;
    private final String message;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}

