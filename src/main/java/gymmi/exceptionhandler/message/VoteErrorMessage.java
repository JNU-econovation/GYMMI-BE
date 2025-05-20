package gymmi.exceptionhandler.message;

import gymmi.global.DuplicationCheckType;
import gymmi.workspace.domain.ObjectionStatus;
import gymmi.workspace.domain.WorkspaceStatus;

public enum VoteErrorMessage implements ErrorMessage {

    ALREADY_VOTED("이미 투표하였습니다.", 400),
    ALREADY_CLOSED_OBJECTION("이미 종료되었습니다", 400),
    ;

    private final String message;
    private final int statusCode;

    VoteErrorMessage(String message, int statusCode) {
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
