package gymmi.exceptionhandler.message;

public enum WorkspaceErrorMessage implements ErrorMessage {

    // 워크스페이스 결과 뽑기
    NOT_COMPLETED_WORKSPACE("종료되지 않은 워크스페이스 입니다.", 400),
    EXIST_OBJECTION_IN_PROGRESS("진행중인 이의신청이 존재합니다.", 400),

    // 워크스페이스 나가기,
    EXIST_WORKERS_EXCLUDE_CREATOR("방장 이외에 참여자가 존재합니다.", 400),

    //
    NOT_REACHED_WORKSPACE_GOAL_SCORE("목표점수를 달성해주세요!", 400);
    private final String message;
    private final int statusCode;

    WorkspaceErrorMessage(String message, int statusCode) {
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
