package gymmi.exceptionhandler.message;

public enum ObjectionErrorMessage implements ErrorMessage {

    ALREADY_OBJECTED("이미 이의 신청되었어요.", 400),
    INACTIVE_WORKSPACE("워크스페이스가 진행중이 아니에요.", 400),
    ;

    private final String message;
    private final int statusCode;

    ObjectionErrorMessage(String message, int statusCode) {
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
