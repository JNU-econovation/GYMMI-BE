package gymmi.workspace.service;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.WorkoutHistoryInWorkspaceValidator;
import gymmi.workspace.domain.entity.*;

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
