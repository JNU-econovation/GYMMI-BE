package gymmi.exceptionhandler.message;

public enum PhotoFeedDeletionErrorMessage implements ErrorMessage {

    NOT_PHOTO_FEED_WRITER("사진피드 작성자가 아니에요", 400);

    private final String message;
    private final int statusCode;

    PhotoFeedDeletionErrorMessage(String message, int statusCode) {
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
