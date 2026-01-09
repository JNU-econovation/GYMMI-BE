package gymmi.workspace.objection.domain;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workout.domain.WorkoutHistoryInWorkspaceValidator;
import gymmi.workspace.workspace.domain.ParticipantValidator;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;

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
