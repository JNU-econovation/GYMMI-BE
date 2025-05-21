package gymmi.exceptionhandler.message;

import gymmi.global.DuplicationCheckType;
import gymmi.workspace.domain.ObjectionStatus;
import gymmi.workspace.domain.WorkspaceStatus;

public enum InvalidQueryParmErrorMessage implements ErrorMessage {
    // common
    UNSUPPORTED_TYPE("지원하지 않는 type 입니다.", 400),
    INVALID_DUPLICATION_CHECK_TYPE_VALUE("잘못된 query param value 입니다. : " + DuplicationCheckType.class.getName(), 400),
    INVALID_WORKSPACE_STATUS_VALUE("잘못된 query param value 입니다. : " + WorkspaceStatus.class.getName(), 400),
    INVALID_OBJECTION_STATUS_VALUE("잘못된 query param value 입니다. : " + ObjectionStatus.class.getName(), 400),
    ;

    private final String message;
    private final int statusCode;

    InvalidQueryParmErrorMessage(String message, int statusCode) {
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
