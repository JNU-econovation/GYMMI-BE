package gymmi.workspace.domain;

import gymmi.exceptionhandler.exception.AlreadyExistException;
import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.domain.entity.ParticipantValidator;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.repository.VoteRepository;
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
