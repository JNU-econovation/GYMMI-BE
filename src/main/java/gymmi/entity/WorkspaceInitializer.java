package gymmi.entity;

import gymmi.request.CreatingWorkspaceRequest;
import gymmi.request.MissionRequest;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class WorkspaceInitializer {
    private Workspace workspace;
    private List<Mission> missions;
    private Task task;
    private Worker worker;

    public void init(User creator, CreatingWorkspaceRequest request) {
        this.workspace = createWorkspace(creator, request);
        this.missions = createMissions(request.getMissionBoard());
        this.task = createTask(creator, request.getTask());
        this.worker = createWorker(creator);
    }

    private Worker createWorker(User creator) {
        return Worker.builder()
                .workspace(workspace)
                .user(creator)
                .task(task)
                .build();
    }

    private Workspace createWorkspace(User creator, CreatingWorkspaceRequest request) {
        return Workspace.builder()
                .creator(creator)
                .name(request.getName())
                .headCount(request.getHeadCount())
                .goalScore(request.getGoalScore())
                .description(request.getDescription())
                .tag(request.getTag())
                .build();
    }

    private List<Mission> createMissions(List<MissionRequest> missionBoard) {
        List<Mission> missions = new ArrayList<>();
        for (MissionRequest missionRequest : missionBoard) {
            Mission mission = Mission.builder()
                    .workspace(workspace)
                    .name(missionRequest.getMission())
                    .score(missionRequest.getScore())
                    .build();
            missions.add(mission);
        }
        return missions;
    }

    private Task createTask(User register, String taskName) {
        return Task.builder()
//                .workspace(workspace)
//                .register(register)
                .name(taskName)
                .build();
    }
}

