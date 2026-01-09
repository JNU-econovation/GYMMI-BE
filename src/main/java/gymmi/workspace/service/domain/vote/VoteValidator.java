package gymmi.workspace.service.domain.vote;

import gymmi.exceptionhandler.exception.AlreadyExistException;
import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.repository.VoteRepository;
import gymmi.workspace.service.domain.objection.Objection;
import gymmi.workspace.service.domain.objection.ObjectionInWorkspaceValidator;
import gymmi.workspace.service.domain.workspace.ParticipantValidator;
import gymmi.workspace.service.domain.workspace.Worker;
import gymmi.workspace.service.domain.workspace.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteValidator {

    private final VoteRepository voteRepository;

    public void validateCanVote(Workspace workspace, Worker worker, Objection objection) {
        ParticipantValidator.validateParticipant(workspace, worker);
        ObjectionInWorkspaceValidator.validate(workspace, objection);

        if (!objection.isInProgress()) {
            throw new InvalidStateException(ErrorCode.ALREADY_CLOSED_OBJECTION);
        }

        if (voteRepository.existsByObjectionIdAndWorkerId(objection.getId(), worker.getId())) {
            throw new AlreadyExistException(ErrorCode.ALREADY_VOTED);

        }
    }

}
