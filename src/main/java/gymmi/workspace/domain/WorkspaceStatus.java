package gymmi.workspace.domain;

public enum WorkspaceStatus {
    PREPARING,
    IN_PROGRESS,
    COMPLETED,        // workspace 종료, task 미 종료
    FULLY_COMPLETED; // task 뽑기 완료
}
