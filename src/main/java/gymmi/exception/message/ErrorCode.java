package gymmi.exception.message;

public enum ErrorCode {

    ALREADY_JOINED_WORKSPACE("이미 참여한 워크스페이스 입니다.", 400),
    ALREADY_USED_LOGIN_ID("이미 사용중인 아이디 입니다.", 400),
    ALREADY_USED_NICKNAME("이미 사용중인 닉네임 입니다.", 400),
    ALREADY_USED_WORKSPACE_NAME("이미 사용중인 워크스페이스 이름 입니다.", 400),
    NOT_FOUND_FILE("해당 파일이 존재하지 않습니다.", 404),
    EMPTY_FILE("파일이 비어있습니다.", 400),
    UNSUPPORTED_FILE("이미지 형식의 파일만 가능합니다.", 400),
    NOT_MATCHED_PASSWORD("비밀번호가 일치하지 않습니다.", 400),


    MISSING_AUTHORIZATION_HEADER("인증 헤더의 값이 비어있습니다.", 401),
    UNSUPPORTED_AUTHORIZATION_TYPE("지원하지 않는 인증 방식 입니다. Bearer 타입으로 인증해 주세요.", 401),
    UNUSUAL_AUTHORIZATION_ACCESS("비정상적인 접근입니다. 다시 로그인 해주세요.", 401),
    FAILED_LOGIN("아이디와 비밀번호를 확인해 주세요.", 401),

    FAILED_FILE_UPLOAD("파일 업로드를 실패하였습니다.", 500),
    FAILED_FILE_DELETION("파일 삭제를 실패하였습니다.", 500),
    MISSING_FILE_EXTENSION("파일의 확장자가 존재하지 않습니다.", 400),
    NOT_MATCHED_JWT_SUBJECT("토큰 제목을 확인해 주세요.", 401),
    EXPIRED_JWT("토큰이 만료되었습니다.", 401),
    JWT_RELATED_ERROR("토큰 관련 에러 발생.", 500),

    EXCEED_MAX_JOINED_WORKSPACE("워크스페이스는 5개까지 참여 가능합니다.(완료된 워크스페이스 제외)", 400),
    NOT_JOINED_WORKSPACE("해당 워크스페이스의 참여자가 아니에요.", 400),
    INACTIVE_WORKSPACE("워크스페이스가 시작중이 아니에요.", 400),
    NOT_WORKSPACE_CREATOR("방장이 아닙니다.", 400),
    NOT_REACHED_WORKSPACE_GOAL_SCORE("목표점수를 달성해주세요!", 400),


    ;

    private final String message;
    private final int statusCode;

    ErrorCode(String message, int statusCode) {
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
