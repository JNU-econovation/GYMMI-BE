package gymmi.workspace.workout.domain;

import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workout.domain.entity.WorkoutRecord;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.workspace.domain.WorkspacePhase;
import gymmi.workspace.workspace.domain.WorkspaceStatus;

import java.util.Collections;
import java.util.List;

public class WorkoutProcessor {
    private final Workspace workspace;
    private final Worker worker;
    private final List<WorkoutRecord> workoutRecords;

    private WorkspacePhase workspacePhase;
    private boolean isPhaseChanged;
    private boolean isApplied;

    public WorkoutProcessor(Workspace workspace, Worker worker, List<WorkoutRecord> workoutRecords) {
        this.workspace = workspace;
        this.worker = worker;
        this.workoutRecords = Collections.unmodifiableList(workoutRecords);
        this.workspacePhase = WorkspacePhase.from(workspace.getGoalScore(), workspace.getCurrentScore());
        this.isApplied = false;
        this.isPhaseChanged = false;
    }

    public void apply(WorkoutValidator validator) {
        if (isApplied) {
            return;
        }
        validator.validateCanWork(workspace, worker);
        addScore();
        updatePhase();
        completeIfGoalHasReached();
    }

    public int getSumScore() {
        return workoutRecords.stream()
                .map(WorkoutRecord::getSum)
                .reduce(0, Integer::sum);
    }

    private void addScore() {
        int score = getSumScore();
        worker.addContributedScore(score);
        workspace.addCurrentScore(score);
        WorkoutHistory workoutHistory = workoutRecords.get(0).getWorkoutHistory();
        workoutHistory.addTotalScore(score);
    }

    private void updatePhase() {
        WorkspacePhase beforeWorkspacePhase = workspacePhase;
        workspacePhase = WorkspacePhase.from(workspace.getGoalScore(), workspace.getCurrentScore());
        if (workspacePhase != beforeWorkspacePhase) {
            isPhaseChanged = true;
        }
    }

    private void completeIfGoalHasReached() {
        if (workspace.hasReachedGoalScore()) {
            workspace.changeStatusTo(WorkspaceStatus.COMPLETED);
        }
    }


    public List<WorkoutRecord> getWorkoutRecords() {
        return workoutRecords;
    }

    public boolean isPhaseChanged() {
        return isPhaseChanged;
    }

    public WorkspacePhase getWorkspacePhase() {
        return workspacePhase;
    }
}
