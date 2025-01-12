package gymmi.exceptionhandler.message;

import gymmi.global.DuplicationCheckType;
import gymmi.workspace.domain.ObjectionStatus;
import gymmi.workspace.domain.WorkspaceStatus;

public enum ErrorCode {

    // workspace
    ALREADY_JOINED_WORKSPACE("이미 참여한 워크스페이스 입니다.", 400),
    ALREADY_USED_WORKSPACE_NAME("이미 사용중인 워크스페이스 이름 입니다.", 400),
    FULL_WORKSPACE("워크스페이스 인원이 가득 찼습니다.", 400),
    NOT_CONSISTENT_WORKERS_COUNT("워크스페이스 인원이 잘못 되었습니다.", 500),
    NOT_CONSISTENT_MISSIONS_COUNT("미션 수가 잘못 되었습니다.", 500),
    ALREADY_ACTIVATED_WORKSPACE("이미 진행중이거나 종료된 워크스페이스 입니다.", 400),
    NOT_COMPLETED_WORKSPACE("종료되지 않은 워크스페이스 입니다.", 400),
    EXIST_WORKERS_EXCLUDE_CREATOR("방장 이외에 참여자가 존재합니다.", 400),
    BELOW_MINIMUM_WORKER("최소 인원인 2명을 채워주세요.", 400),
    EXCEED_MAX_JOINED_WORKSPACE("워크스페이스는 5개까지 참여 가능합니다.(완료된 워크스페이스 제외)", 400),
    NOT_JOINED_WORKSPACE("해당 워크스페이스의 참여자가 아니에요.", 403),
    EXIST_NOT_JOINED_WORKER("해당 워크스페이스의 참여자가 아닌 사람이 존재합니다.", 500),
    INACTIVE_WORKSPACE("워크스페이스가 진행중이 아니에요.", 400),
    NOT_WORKSPACE_CREATOR("해당 워크스페이스의 방장이 아닙니다.", 403),
    NOT_REACHED_WORKSPACE_GOAL_SCORE("목표점수를 달성해주세요!", 400),
    NOT_REGISTERED_WORKSPACE_MISSION("워크스페이스에 등록된 미션이 아닙니다.", 400),
    EXIST_NOT_REGISTERED_MISSION("워크스페이스에 등록 되지 않은 미션이 존재합니다.", 500),
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
    NO_WORKOUT_HISTORY_EXIST_IN_WORKSPACE("해당 워크스페이스의 운동 기록이 아니에요", 403),
    NO_OBJECTION_EXIST_IN_WORKSPACE("해당 워크스페이스와 관련 정보가 아니에요", 403),
    ALREADY_OBJECTED("이미 이의 신청되었어요.", 400),
    NOT_FOUND_OBJECTION("해당 이의 신청이 존재하지 않습니다.", 404),
    ALREADY_VOTED("이미 투표하였습니다.", 400),
    ALREADY_CLOSED_OBJECTION("이미 종료되었습니다", 400),

    // 운동 기록
    EXCEED_MAX_DAILY_WORKOUT_HISTORY_COUNT("일일 가능한 운동을 초과했어요! (3회까지 가능)", 400),

    // 이의 신청
    EXIST_OBJECTION_IN_PROGRESS("진행중인 이의신청이 존재합니다.", 400),


    // user
    ALREADY_USED_LOGIN_ID("이미 사용중인 아이디 입니다.", 400),
    ALREADY_USED_NICKNAME("이미 사용중인 닉네임 입니다.", 400),

    // login
    MISSING_AUTHORIZATION_HEADER("인증 헤더의 값이 비어있습니다.", 401),
    UNSUPPORTED_AUTHORIZATION_TYPE("지원하지 않는 인증 방식 입니다. Bearer 타입으로 인증해 주세요.", 401),
    UNUSUAL_AUTHORIZATION_ACCESS("비정상적인 접근입니다. 다시 로그인 해주세요.", 401),
    FAILED_LOGIN("아이디와 비밀번호를 확인해 주세요.", 401),
    NOT_MATCHED_JWT_SUBJECT("토큰 제목을 확인해 주세요.", 401),
    EXPIRED_JWT("토큰이 만료되었습니다.", 401),
    JWT_RELATED_ERROR("토큰 관련 에러 발생.", 500),

    // file
    NOT_FOUND_FILE("해당 파일이 존재하지 않습니다.", 404),
    EMPTY_FILE("파일이 비어있습니다.", 400),
    UNSUPPORTED_FILE("이미지 형식의 파일만 가능합니다.", 400),
    FAILED_FILE_UPLOAD("파일 업로드를 실패하였습니다.", 500),
    FAILED_FILE_DELETION("파일 삭제를 실패하였습니다.", 500),
    MISSING_FILE_EXTENSION("파일의 확장자가 존재하지 않습니다.", 400),

    // common
    NOT_MATCHED_PASSWORD("비밀번호가 일치하지 않습니다.", 400),
    UNSUPPORTED_TYPE("지원하지 않는 type 입니다.", 400),
    NOT_FOUND_WORKER("존재하지 않는 참여자 입니다.", 400),
    NOT_FOUND_IMAGE_OBJECT("존재하지 않은 이미지 object 입니다.", 400),
    INVALID_DUPLICATION_CHECK_TYPE_VALUE("잘못된 query param value 입니다. : " + DuplicationCheckType.class.getName(), 400),
    INVALID_WORKSPACE_STATUS_VALUE("잘못된 query param value 입니다. : " + WorkspaceStatus.class.getName(), 400),
    INVALID_OBJECTION_STATUS_VALUE("잘못된 query param value 입니다. : " + ObjectionStatus.class.getName(), 400),

    // photo feed
    NOT_FOUND_PHOTO_FEED("존재하지 않는 사진피드 입니다.", 400),
    NOT_FOUND_PHOTO_FEED_IMAGE("존재하지 않는 사진 입니다.", 400),
    NOT_PHOTO_FEED_WRITER("사진피드 작성자가 아니에요", 400),

    // etc
    RETRY_AFTER_LOGOUT("로그아웃 후 다시 시도해 주세요", 500),
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
