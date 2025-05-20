package gymmi.exceptionhandler.message;

public enum FileErrorMessage implements ErrorMessage {

    NOT_FOUND_FILE("해당 파일이 존재하지 않습니다.", 404),
    EMPTY_FILE("파일이 비어있습니다.", 400),
    UNSUPPORTED_FILE("이미지 형식의 파일만 가능합니다.", 400),
    FAILED_FILE_UPLOAD("파일 업로드를 실패하였습니다.", 500),
    FAILED_FILE_DELETION("파일 삭제를 실패하였습니다.", 500),
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
