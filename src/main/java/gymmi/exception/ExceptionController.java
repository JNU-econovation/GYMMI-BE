package gymmi.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle400Exception(BadRequestException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getMessage());
        log.warn(System.lineSeparator() +
                        "[에러 발생 로그]" + System.lineSeparator() +
                        "request-url : {}" + System.lineSeparator() +
                        "error: {}",
                request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity.badRequest().body(response);
    }

}
