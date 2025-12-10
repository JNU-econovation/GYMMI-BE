package gymmi.workspace.service;

import gymmi.workspace.domain.entity.Mission;
import gymmi.workspace.repository.MissionRepository;
import gymmi.workspace.request.WorkingMissionInWorkspaceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WorkoutRequestMapper {

    private final MissionRepository missionRepository;

    public Map<Mission, Integer> getWorkouts(List<WorkingMissionInWorkspaceRequest> requests) {
        Map<Mission, Integer> workouts = new HashMap<>();
        for (WorkingMissionInWorkspaceRequest request : requests) {
            Mission mission = missionRepository.findByIdOrThrow(request.getId());
            workouts.put(mission, request.getCount());
        }
        return workouts;
    }

}

