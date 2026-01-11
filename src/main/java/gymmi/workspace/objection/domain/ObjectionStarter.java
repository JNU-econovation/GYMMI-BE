package gymmi.workspace.objection.domain;

import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workout.domain.WorkoutHistoryInWorkspaceValidator;
import gymmi.workspace.workspace.domain.ParticipantValidator;
import gymmi.workspace.workspace.domain.WorkspaceStatusValidator;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;

public class ObjectionStarter {

    public Objection execute(Workspace workspace, Worker subject, WorkoutHistory workoutHistory, String reason) {
        ParticipantValidator.validateParticipant(workspace, subject);
        WorkspaceStatusValidator.validateWorkspaceIsInProgress(workspace);
        WorkoutHistoryInWorkspaceValidator.validateWorkoutHistoryInWorkspace(workspace, workoutHistory);

        return Objection.builder()
                .subject(subject)
                .reason(reason)
                .workoutHistory(workoutHistory)
                .build();
    }
}
