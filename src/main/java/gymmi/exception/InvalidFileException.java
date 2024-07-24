package gymmi.exception;

public class InvalidFileException extends BadRequestException {

    public static final String ERROR_CODE = "INVALID_FILE";
    public static final String ERROR_DESCRIPTION = "잘못된 파일의 요청인 경우";

    public InvalidFileException(String message) {
        super(message, ERROR_CODE, ERROR_DESCRIPTION);
    }
}
