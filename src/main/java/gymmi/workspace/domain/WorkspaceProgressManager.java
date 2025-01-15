package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static gymmi.exceptionhandler.message.ErrorCode.NOT_JOINED_WORKSPACE;

public class WorkspaceProgressManager {

    private final Workspace workspace;
    private final List<Mission> missions;
    private WorkspacePhase workspacePhase;

    public WorkspaceProgressManager(Workspace workspace, List<Mission> missions, int achievementScore) {
        WorkspaceWithMissionsConsistencyValidator.validateRegistration(workspace, missions);
        WorkspaceWithMissionsConsistencyValidator.validateConsistencyMissionsCount(missions);
        this.workspace = validateStatus(workspace);
        this.missions = Collections.unmodifiableList(missions);
        this.workspacePhase = WorkspacePhase.from(workspace.getGoalScore(), achievementScore);
    }

    private Workspace validateStatus(Workspace workspace) {
        if (!workspace.isInProgress()) {
            throw new InvalidStateException(ErrorCode.INACTIVE_WORKSPACE);
        }
        return workspace;
    }

    public WorkoutHistory doWorkout(Worker worker, Map<Mission, Integer> workouts, WorkoutConfirmation workoutProof) {
        if (!worker.isJoinedIn(workspace)) {
            throw new InvalidStateException(NOT_JOINED_WORKSPACE);
        }
        List<WorkoutRecord> workoutRecords = workouts.entrySet().stream()
                .map(workout -> doMission(workout.getKey(), workout.getValue()))
                .toList();
        return new WorkoutHistory(worker, workoutRecords, workoutProof);
    }


    private WorkoutRecord doMission(Mission mission, int count) {
        if (!missions.contains(mission)) {
            throw new InvalidStateException(ErrorCode.NOT_REGISTERED_WORKSPACE_MISSION);
        }
        return new WorkoutRecord(mission, count);
    }

    public void completeWhenGoalScoreIsAchieved(int achievementScore) {
        if (workspace.isMoreThan(achievementScore)) {
            return;
        }
        workspace.changeStatusTo(WorkspaceStatus.COMPLETED);
    }

    public boolean hasPhaseChanged(int achievementScore) {
        WorkspacePhase newWorkspacePhase = WorkspacePhase.from(workspace.getGoalScore(), achievementScore);
        if (this.workspacePhase == newWorkspacePhase) {
            return false;
        }
        this.workspacePhase = newWorkspacePhase;
        return true;
    }

    public WorkspacePhase getWorkspacePhase() {
        return workspacePhase;
    }
}
