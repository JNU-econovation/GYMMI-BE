package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;

import java.util.Arrays;
import java.util.Comparator;

public enum WorkspacePhase {

    P_0(0),
    P_25(25),
    P_50(50),
    P_75(75),
    P_100(100);

    private final int value;

    WorkspacePhase(int value) {
        this.value = value;
    }

    public static WorkspacePhase from(int goalScore, int achievementScore) {
        int phasePercent = (int) ((achievementScore / (double) goalScore) * 100);
        return Arrays.stream(values())
                .filter(workspacePhase -> workspacePhase.isLowerOrEqualThan(phasePercent))
                .max(Comparator.comparingInt(WorkspacePhase::getValue))
                .orElseThrow(() -> new InvalidStateException(ErrorCode.LOGIC_ERROR));
    }

    private boolean isLowerOrEqualThan(int value) {
        return this.value <= value;
    }

    public int getValue() {
        return value;
    }
}
