package gymmi.workspace.response;

import gymmi.entity.User;
import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InsideWorkspaceResponse {

    private final String name;
    private final Integer headCount;
    private final String status;
    private final Boolean isObjectionInProgress;
    private final Integer goalScore;
    private final String description;
    private final Integer achievementScore;
    private final Boolean isCreator;
    private final List<WorkerResponse> workers;

    @Builder
    public InsideWorkspaceResponse(
            Workspace workspace,
            List<Worker> workers,
            int achievementScore,
            boolean isObjectionInProgress,
            User loginedUser
    ) {
        this.name = workspace.getName();
        this.headCount = workspace.getHeadCount();
        this.status = workspace.getStatus().name();
        this.goalScore = workspace.getGoalScore();
        this.description = workspace.getDescription();
        this.achievementScore = achievementScore;
        this.isObjectionInProgress = isObjectionInProgress;
        this.isCreator = workspace.isCreatedBy(loginedUser);
        this.workers = getWorkerResponse(workspace, workers, loginedUser);
    }

    private List<WorkerResponse> getWorkerResponse(
            Workspace workspace,
            List<Worker> workers,
            User loginedUser
    ) {
        List<WorkerResponse> workerResponses = new ArrayList<>();
        for (int i = 0; i < workers.size(); i++) {
            Worker worker = workers.get(i);
            workerResponses.add(
                    new WorkerResponse(worker, i, workspace.isCreatedBy(worker), loginedUser.equals(worker.getUser()))
            );
        }
        return workerResponses;
    }

}
