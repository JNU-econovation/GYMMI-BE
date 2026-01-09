package gymmi.workspace.workout.service;

import gymmi.workspace.mission.repository.MissionRepository;
import gymmi.workspace.workout.controller.request.WorkingMissionInWorkspaceRequest;
import gymmi.workspace.workout.controller.request.WorkoutRequest;
import gymmi.workspace.mission.domain.entity.Mission;
import gymmi.workspace.mission.domain.MissionExistenceValidator;
import gymmi.workspace.workout.domain.entity.WorkoutConfirmation;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workout.domain.entity.WorkoutRecord;
import gymmi.workspace.workspace.domain.entity.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WorkoutRequestMapper {

    private final MissionRepository missionRepository;

    public WorkoutConfirmation createWorkoutConfirmation(WorkoutRequest workoutRequest) {
        return new WorkoutConfirmation(workoutRequest.getImageUrl(), workoutRequest.getComment());
    }

    public List<WorkoutRecord> createWorkoutRecords(Workspace workspace, WorkoutHistory workoutHistory, List<WorkingMissionInWorkspaceRequest> requests) {
        validateMissions(workspace, requests);
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

    private void validateMissions(Workspace workspace, List<WorkingMissionInWorkspaceRequest> requests) {
        List<Long> missionIds = requests.stream()
                .map(WorkingMissionInWorkspaceRequest::getId)
                .toList();
        List<Mission> missions = missionRepository.findAllById(missionIds);
        MissionExistenceValidator.validateMissionExistenceInWorkspace(workspace, missions);
    }


}

