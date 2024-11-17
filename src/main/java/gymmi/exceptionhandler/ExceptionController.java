package gymmi.exceptionhandler;

import gymmi.exceptionhandler.exception.GymmiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAllCustom(GymmiException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(e.getExceptionCode().name(), e.getMessage());
        log(e, request.getRequestURI());
        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAll(Exception e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("백엔드에게 문의하세요", e.getMessage());
        log(e, request.getRequestURI());
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle400Exception(ConstraintViolationException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse("INVALID_INPUT_VALUE", e.getMessage());
        log(e, request.getRequestURI());
        return ResponseEntity.badRequest().body(response);
    }

    private void log(Exception e, String requestURI) {
        log.warn(System.lineSeparator() +
                        "[에러 발생 로그]" + System.lineSeparator() +
                        "request-url : {}" + System.lineSeparator() +
                        "error: {}",
                requestURI, e.getMessage(), e);
    }

}
