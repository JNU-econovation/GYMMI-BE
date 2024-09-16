package gymmi.exception.message;

public enum ExceptionCode {
    //@formatter:off
    ALREADY_EXISTS("(어떠한 것이든) 이미 존재하는 경우"),

    NOT_FOUND("(어떠한 것 이든) 찾고자하는 것이 존재 하지 않는 경우"),

    NOT_MATCHED("(어떠한 것이든) 일치 하지 않는 경우"),
        AUTHENTICATION_FAIL("인증에 실패한 경우"),

    UNSUPPORTED("(어떠한 것이든) 지원하지 않는 경우"),

    FILE_RELATED("파일과 관련된 경우"),
        INVALID_FILE("잘못된 파일인 경우"),
        FILE_IO_FAIL("파일 입출력이 실패한 경우"),

    INVALID_NUMBER("조건에 맞지 않는 숫자인 경우"),

    INVALID_PATTERN("조건에 맞지 않는 문자열인 경우"),

    INVALID_RANGE("조건에 맞지 않는 범위인 경우"),

    INVALID_STATE("조건에 맞지 않는 상태인 경우"),

    NOT_HAVE_PERMISSION("권한이 없는 경우");

    //@formatter:off
    private final String description;

    ExceptionCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
