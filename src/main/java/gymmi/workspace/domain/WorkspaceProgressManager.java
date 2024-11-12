package gymmi.workspace.domain;

import static gymmi.exceptionhandler.message.ErrorCode.NOT_JOINED_WORKSPACE;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.domain.entity.WorkoutConfirmation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WorkspaceProgressManager {

    private final Workspace workspace;
    private final List<Mission> missions;


    public WorkspaceProgressManager(Workspace workspace, List<Mission> missions) {
        WorkspaceWithMissionsConsistencyValidator.validateRegistration(workspace, missions);
        WorkspaceWithMissionsConsistencyValidator.validateConsistencyMissionsCount(missions);
        this.workspace = validateStatus(workspace);
        this.missions = Collections.unmodifiableList(missions);
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

}
