package gymmi.response;

import gymmi.entity.User;
import gymmi.entity.Worker;
import gymmi.entity.Workspace;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InsideWorkspaceResponse {

    private final String name;
    private final Integer headCount;
    private final String status;
    private final Integer goalScore;
    private final String description;
    private final Integer achievementScore;
    private final List<WorkerResponse> workers;

    @Builder
    public InsideWorkspaceResponse(
            Workspace workspace, Integer achievementScore,
            List<Worker> sortedWorkers, List<Integer> workerRanks,
            User loginedUser
    ) {
        this.name = workspace.getName();
        this.headCount = workspace.getHeadCount();
        this.status = workspace.getStatus().name();
        this.goalScore = workspace.getGoalScore();
        this.description = workspace.getDescription();
        this.achievementScore = achievementScore;
        this.workers = getWorkerResponse(sortedWorkers, workerRanks, workspace.getCreator(), loginedUser);
    }

    private List<WorkerResponse> getWorkerResponse(
            List<Worker> sortedWorkers, List<Integer> workerRanks,
            User creator, User loginedUser
    ) {
        List<WorkerResponse> workerResponses = new ArrayList<>();
        for (int i = 0; i < sortedWorkers.size(); i++) {
            Worker worker = sortedWorkers.get(i);
            WorkerResponse response = WorkerResponse.builder()
                    .worker(worker)
                    .rank(workerRanks.get(i))
                    .isCreator(creator.equals(worker.getUser()))
                    .isMyself(worker.getUser().equals(loginedUser))
                    .build();
            workerResponses.add(response);
        }
        return workerResponses;
    }

}
