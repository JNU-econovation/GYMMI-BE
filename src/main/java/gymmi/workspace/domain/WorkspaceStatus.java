package gymmi.workspace.domain;

public enum WorkspaceStatus {
    PREPARING,
    IN_PROGRESS,
    COMPLETED,        // workspace 종료, 당첨,벌칙 미 종료
    FULLY_COMPLETED; // workspace 종료, 당첨,벌칙 뽑기 완료
}
