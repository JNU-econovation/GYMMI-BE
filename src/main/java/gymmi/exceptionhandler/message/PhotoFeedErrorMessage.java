package gymmi.exceptionhandler.message;

public enum PhotoFeedErrorMessage implements ErrorMessage {

    NOT_FOUND_PHOTO_FEED("존재하지 않는 사진피드 입니다.", 400),
    NOT_FOUND_PHOTO_FEED_IMAGE("존재하지 않는 사진 입니다.", 400),
    NOT_PHOTO_FEED_WRITER("사진피드 작성자가 아니에요", 400);

    private final String message;
    private final int statusCode;

    PhotoFeedErrorMessage(String message, int statusCode) {
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
