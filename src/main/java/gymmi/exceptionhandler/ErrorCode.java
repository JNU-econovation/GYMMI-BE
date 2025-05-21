package gymmi.exceptionhandler;

public enum ErrorCode {

    // 인증, 회원가입
    AUTH_FAIL("사용자 인증 실패", 401),
    REGISTER_FAIL("회원가입 실패", 400),

    // 워크스페이스
    WORKSPACE_START_FAIL("워크스페이스 시작 실패", 400),
    WORKSPACE_JOIN_FAIL("워크스페이스 참여 실패", 400),
    WORKSPACE_EDIT_FAIL("워크스페이스 수정 실패", 400),
    WORKSPACE_CREATION_FAIL("워크스페이스 생성 실패", 400),
    WORKSPACE_DRAW_FAIL("워크스페이스 결과 확인 실패", 400),
    WORKSPACE_EXIT_FAIL("워크스페이스 나가기 실패", 400),

    //
    PERMISSION_DENIED("권한 없음.", 400),

    //
    INVALID_ACCESS("유효하지 않는 접근.", 403),

    // 운동 기록
    WORKOUT_FAIL("운동 기록 등록 실패", 400),

    // 이의 신청 투표
    VOTE_FAIL("이의 신청 투표 실패", 400),

    // 이의 신청
    OBJECTION_FAIL("이의 신청 실패", 400),

    // 사진 피드
    PHOTO_FEED_DELETION_FAIL("사진 피드 삭제 실패", 400),

    //
    NOT_FOUND("존재하지 않는 리소스", 404),

    //
    INVALID_FILE("유효하지 않는 파일", 400),

    //
    INVALID_QUERY_PARM("유효하지 않는 파라미터 키 또는 값", 400),

    //
    SERVER_ERROR("확인되지 않은 에러. 백엔드에 문의해주세요", 500);

    private final String value;
    private final int statusCode;

    ErrorCode(String value, int statusCode) {
        this.value = value;
        this.statusCode = statusCode;
    }
}
