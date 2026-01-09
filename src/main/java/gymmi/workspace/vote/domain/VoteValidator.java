package gymmi.workspace.vote.domain;

import gymmi.global.exceptionhandler.exception.AlreadyExistException;
import gymmi.global.exceptionhandler.exception.InvalidStateException;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.workspace.vote.repository.VoteRepository;
import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.objection.domain.ObjectionInWorkspaceValidator;
import gymmi.workspace.workspace.domain.ParticipantValidator;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
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
