package gymmi.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle400Exception(BadRequestException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getMessage());
        log(e, request.getRequestURI());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle403Exception(AuthenticationException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getMessage());
        log(e, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    private void log(GymmiException e, String requestURI) {
        log.warn(System.lineSeparator() +
                        "[에러 발생 로그]" + System.lineSeparator() +
                        "request-url : {}" + System.lineSeparator() +
                        "error: {}",
                requestURI, e.getMessage(), e);
    }


}
