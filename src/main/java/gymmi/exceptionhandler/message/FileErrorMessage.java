package gymmi.exceptionhandler.message;

public enum FileErrorMessage implements ErrorMessage {

    EMPTY_FILE("파일이 비어있습니다.", 400),
    UNSUPPORTED_FILE("이미지 형식의 파일만 가능합니다.", 400),
    MISSING_FILE_EXTENSION("파일의 확장자가 존재하지 않습니다.", 400),
    ;

    private final String message;
    private final int statusCode;

    FileErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
