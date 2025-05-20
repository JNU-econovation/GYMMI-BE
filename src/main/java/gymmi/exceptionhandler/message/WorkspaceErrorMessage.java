package gymmi.exceptionhandler.message;

public enum WorkspaceErrorMessage implements ErrorMessage {

    // etc
    NOT_CONSISTENT_WORKERS_COUNT("워크스페이스 인원이 잘못 되었습니다.", 500),
    NOT_CONSISTENT_MISSIONS_COUNT("미션 수가 잘못 되었습니다.", 500),
    EXIST_NOT_JOINED_WORKER("해당 워크스페이스의 참여자가 아닌 사람이 존재합니다.", 500),
    EXIST_NOT_REGISTERED_MISSION("워크스페이스에 등록 되지 않은 미션이 존재합니다.", 500),

    // common
    NOT_JOINED_WORKSPACE("해당 워크스페이스의 참여자가 아니에요.", 403),

    // 워크스페이스 결과 뽑기
    NOT_COMPLETED_WORKSPACE("종료되지 않은 워크스페이스 입니다.", 400),

    // 워크스페이스 종료
    EXIST_OBJECTION_IN_PROGRESS("진행중인 이의신청이 존재합니다.", 400),

    // 워크스페이스 나가기
    EXIST_WORKERS_EXCLUDE_CREATOR("방장 이외에 참여자가 존재합니다.", 400),

    // 워크스페이스 시작
    BELOW_MINIMUM_WORKER("최소 인원인 2명을 채워주세요.", 400),
    NOT_WORKSPACE_CREATOR("해당 워크스페이스의 방장이 아닙니다.", 403),



    // 워크스페이스 조회
    NO_WORKOUT_HISTORY_EXIST_IN_WORKSPACE("해당 워크스페이스의 운동 기록이 아니에요", 403),
    NO_OBJECTION_EXIST_IN_WORKSPACE("해당 워크스페이스와 관련 정보가 아니에요", 403),



    NOT_REACHED_WORKSPACE_GOAL_SCORE("목표점수를 달성해주세요!", 400),

    ;
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
