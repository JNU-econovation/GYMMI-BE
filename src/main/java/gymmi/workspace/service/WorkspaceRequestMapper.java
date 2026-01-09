package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.workspace.service.domain.mission.Missions;
import gymmi.workspace.service.domain.mission.Mission;
import gymmi.workspace.service.domain.workspace.Workspace;
import gymmi.workspace.request.CreatingWorkspaceRequest;
import gymmi.workspace.request.MissionRequest;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceRequestMapper {

    public static Workspace createFrom(User user, CreatingWorkspaceRequest request) {
        return Workspace.builder()
                .creator(user)
                .name(request.getName())
                .headCount(request.getHeadCount())
                .goalScore(request.getGoalScore())
                .description(request.getDescription())
                .tag(request.getTag())
                .task(request.getTask())
                .build();
    }

    public static Missions createFrom(Workspace workspace, List<MissionRequest> requests) {
        List<Mission> missions = new ArrayList<>();
        for (MissionRequest missionRequest : requests) {
            Mission mission = Mission.builder()
                    .workspace(workspace)
                    .name(missionRequest.getMission())
                    .score(missionRequest.getScore())
                    .build();
            missions.add(mission);
        }
        return new Missions(missions);
    }
}

