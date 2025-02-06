package gymmi.workspace.response;

import gymmi.workspace.domain.entity.Worker;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class WorkspaceResultResponse {

    private final String task;
    private final List<WorkerResultResponse> workers;

    public WorkspaceResultResponse(String task, List<Worker> workers) {
        this.task = task;
        this.workers = getWorkerResultResponse(workers);
    }

    private List<WorkerResultResponse> getWorkerResultResponse(List<Worker> workers) {
        List<WorkerResultResponse> workerResultResponse = new ArrayList<>();
        for (int i = 0; i < workers.size(); i++) {
            workerResultResponse.add(new WorkerResultResponse(workers.get(i), i + 1));
        }
        return workerResultResponse;
    }
}
