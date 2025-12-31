package gymmi.workspace.service;

import gymmi.workspace.domain.MissionExistenceValidator;
import gymmi.workspace.domain.entity.Mission;
import gymmi.workspace.domain.entity.WorkoutConfirmation;
import gymmi.workspace.domain.entity.WorkoutHistory;
import gymmi.workspace.domain.entity.WorkoutRecord;
import gymmi.workspace.repository.MissionRepository;
import gymmi.workspace.request.WorkingMissionInWorkspaceRequest;
import gymmi.workspace.request.WorkoutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WorkoutRequestMapper {

    private final MissionRepository missionRepository;
    private final MissionExistenceValidator missionExistenceValidator;

    public WorkoutConfirmation createWorkoutConfirmation(WorkoutRequest workoutRequest) {
        return new WorkoutConfirmation(workoutRequest.getImageUrl(), workoutRequest.getComment());
    }

    public List<WorkoutRecord> createWorkoutRecords(Long workspaceId, WorkoutHistory workoutHistory, List<WorkingMissionInWorkspaceRequest> requests) {
        validateMissions(workspaceId, requests);
        Map<Mission, Integer> missionsWithCounts = mapMissionsToCounts(requests);
        return createAllWorkoutRecord(workoutHistory, missionsWithCounts);
    }

    private List<WorkoutRecord> createAllWorkoutRecord(WorkoutHistory workoutHistory, Map<Mission, Integer> missionsWithCounts) {
        return missionsWithCounts.entrySet().stream()
                .map(missionWithCount -> new WorkoutRecord(workoutHistory, missionWithCount.getKey(), missionWithCount.getValue()))
                .toList();
    }

    private Map<Mission, Integer> mapMissionsToCounts(List<WorkingMissionInWorkspaceRequest> requests) {
        Map<Mission, Integer> workouts = new HashMap<>();
        for (WorkingMissionInWorkspaceRequest request : requests) {
            Mission mission = missionRepository.findByIdOrThrow(request.getId());
            workouts.put(mission, request.getCount());
        }
        return workouts;
    }

    private void validateMissions(Long workspaceId, List<WorkingMissionInWorkspaceRequest> requests) {
        List<Long> missionIds = requests.stream()
                .map(WorkingMissionInWorkspaceRequest::getId)
                .toList();
        missionExistenceValidator.validateMissionExistenceInWorkspace(workspaceId, missionIds);
    }


}

