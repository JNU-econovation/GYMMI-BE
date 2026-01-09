package gymmi.workspace.service.domain.objection;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.service.domain.workout.WorkoutHistory;
import gymmi.workspace.service.domain.workout.WorkoutHistoryInWorkspaceValidator;
import gymmi.workspace.service.domain.workspace.ParticipantValidator;
import gymmi.workspace.service.domain.workspace.Worker;
import gymmi.workspace.service.domain.workspace.Workspace;

public class ObjectionStarter {

    public Objection execute(Workspace workspace, Worker subject, WorkoutHistory workoutHistory, String reason, ObjectionAlreadyOpenValidator validator) {
        validator.validate(workoutHistory.getId());
        ParticipantValidator.validateParticipant(workspace, subject);

        if (!workspace.isInProgress()) {
            throw new InvalidStateException(ErrorCode.INACTIVE_WORKSPACE);
        }

        WorkoutHistoryInWorkspaceValidator.validate(workspace, workoutHistory);

        return Objection.builder()
                .subject(subject)
                .reason(reason)
                .workoutHistory(workoutHistory)
                .build();
    }
}
