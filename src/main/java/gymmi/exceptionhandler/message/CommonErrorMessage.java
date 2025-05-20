package gymmi.exceptionhandler.message;

import gymmi.global.DuplicationCheckType;
import gymmi.workspace.domain.ObjectionStatus;
import gymmi.workspace.domain.WorkspaceStatus;

public enum CommonErrorMessage implements ErrorMessage {
    // common
    UNSUPPORTED_TYPE("지원하지 않는 type 입니다.", 400),
    NOT_FOUND_WORKER("존재하지 않는 참여자 입니다.", 400),
    NOT_FOUND_IMAGE_OBJECT("존재하지 않은 이미지 object 입니다.", 400),
    INVALID_DUPLICATION_CHECK_TYPE_VALUE("잘못된 query param value 입니다. : " + DuplicationCheckType.class.getName(), 400),
    INVALID_WORKSPACE_STATUS_VALUE("잘못된 query param value 입니다. : " + WorkspaceStatus.class.getName(), 400),
    INVALID_OBJECTION_STATUS_VALUE("잘못된 query param value 입니다. : " + ObjectionStatus.class.getName(), 400),

    // etc
    RETRY_AFTER_LOGOUT("로그아웃 후 다시 시도해 주세요", 500),
    LOGIC_ERROR("벡엔드 문의 바람", 500),
    ;

    private final String message;
    private final int statusCode;

    CommonErrorMessage(String message, int statusCode) {
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
