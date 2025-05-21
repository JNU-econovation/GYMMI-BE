package gymmi.exceptionhandler;

import gymmi.exceptionhandler.exception.GymmiException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAllCustom(GymmiException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(e);
        log(e, errorCode);
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    private void log(Exception e, ErrorCode errorCode) {
        if (errorCode.getStatusCode() >= 500) {
            log.error(errorCode.name(), e);
            return;
        }
        log.info(errorCode.name(), e);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAll(Exception e) {
        ErrorCode errorCode = ErrorCode.NOT_HANDLED_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(errorCode, e.getMessage());
        log.error(errorCode.name(), e);
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle400Exception(ConstraintViolationException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        ErrorResponse errorResponse = new ErrorResponse(errorCode, e.getMessage());
        log.info(errorCode.name(), e);
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }

}
