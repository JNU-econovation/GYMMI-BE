package gymmi.exceptionhandler.message;

public enum WorkspaceCreationErrorMessage implements ErrorMessage {

    // 워크스페이스 생성
    ALREADY_USED_WORKSPACE_NAME("이미 사용중인 워크스페이스 이름 입니다.", 400),
    INVALID_MISSION_SCORE_UNIT("미션 점수는 10점 단위로 입력해주세요.", 400),
    INVALID_WORKSPACE_NAME_LENGTH("워크스페이스 이름은 9자까지 가능합니다.", 400),
    INVALID_WORKSPACE_NAME_FORMAT("워크스페이스 이름은 한글, 영문, 숫자만 가능합니다.", 400),
    INVALID_WORKSPACE_HEAD_COUNT("워크스페이스 인원수는 2~9명까지 가능합니다.", 400),
    INVALID_TAG_NAME_LENGTH("태그의 글자수는 10자까지 가능합니다.", 400),
    INVALID_TAG_NAME_FORMAT("태그는 한글, 영어만 가능합니다.", 400),
    INVALID_WORKSPACE_MISSION_SIZE("미션 개수는 1~15개까지 가능합니다.", 400),
    INVALID_WORKSPACE_MISSION_NAME_LENGTH("미션 글자수는 20자까지 가능합니다.", 400),
    INVALID_WORKSPACE_GOAL_SCORE("목표점수는 100점에서 1000점까지 가능합니다.", 400),
    INVALID_WORKSPACE_MISSION_SCORE("미션 점수는 1~10점까지 가능합니다.", 400),
    ;

    private final String message;
    private final int statusCode;

    WorkspaceCreationErrorMessage(String message, int statusCode) {
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
