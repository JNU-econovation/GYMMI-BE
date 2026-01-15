package gymmi.workspace.vote.service;

import gymmi.user.domain.User;
import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.objection.repository.ObjectionRepository;
import gymmi.workspace.vote.repository.VoteRepository;
import gymmi.workspace.workspace.repository.WorkerRepository;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import gymmi.workspace.vote.domain.entity.Vote;
import gymmi.workspace.vote.domain.VoteManger;
import gymmi.workspace.vote.domain.VoteReflector;
import gymmi.workspace.vote.domain.VoteValidator;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final ObjectionRepository objectionRepository;
    private final VoteValidator voteValidator;
    private final VoteRepository voteRepository;

    public void voteToObjection(User loginedUser, Long workspaceId, Long objectionId, boolean willApprove) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspaceId);

        Objection objection = objectionRepository.findByIdOrThrow(objectionId);

        VoteManger voteManger = new VoteManger();
        Vote vote = voteManger.createVote(workspace, objection, worker, willApprove, voteValidator);

        voteRepository.save(vote);

        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspaceId);
        List<Vote> votes = voteRepository.findAllByObjectionId(objectionId);

        VoteReflector voteReflector = new VoteReflector(objection, votes, workers.size());
        voteReflector.apply();

    }
}
