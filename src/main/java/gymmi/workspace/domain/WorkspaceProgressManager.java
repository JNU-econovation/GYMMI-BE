package gymmi.workspace.domain;

import gymmi.exception.class1.InvalidStateException;
import gymmi.exception.message.ErrorCode;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static gymmi.exception.message.ErrorCode.NOT_JOINED_WORKSPACE;

public class WorkspaceProgressManager {

    private final Workspace workspace;
    private final List<Mission> missions;


    public WorkspaceProgressManager(Workspace workspace, List<Mission> missions) {
        this.workspace = validateStatus(workspace);
        this.missions = validateRegistration(missions);
    }

    private Workspace validateStatus(Workspace workspace) {
        if (!workspace.isInProgress()) {
            throw new InvalidStateException(ErrorCode.INACTIVE_WORKSPACE);
        }
        return workspace;
    }

    private List<Mission> validateRegistration(List<Mission> missions) {
        if (!missions.stream()
                .allMatch(mission -> mission.isRegisteredIn(workspace))) {
            throw new InvalidStateException(ErrorCode.NOT_REGISTERED_WORKSPACE_MISSION);
        }
        return Collections.unmodifiableList(missions);
    }

    public Worked doWorkout(Worker worker, Map<Mission, Integer> workouts) {
        if (!worker.isJoinedIn(workspace)) {
            throw new InvalidStateException(NOT_JOINED_WORKSPACE);
        }
        List<WorkoutRecord> workoutRecords = workouts.entrySet().stream()
                .map(workout -> doMission(workout.getKey(), workout.getValue()))
                .toList();
        return new Worked(worker, workoutRecords);
    }

    private WorkoutRecord doMission(Mission mission, int count) {
        if (missions.contains(mission)) {
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
