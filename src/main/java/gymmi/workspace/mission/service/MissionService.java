package gymmi.workspace.mission.service;

import gymmi.entity.User;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.mission.domain.entity.FavoriteMission;
import gymmi.workspace.mission.domain.entity.Mission;
import gymmi.workspace.mission.repository.FavoriteMissionRepository;
import gymmi.workspace.mission.repository.MissionRepository;
import gymmi.workspace.workspace.repository.WorkerRepository;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import gymmi.workspace.mission.controller.response.FavoriteMissionResponse;
import gymmi.workspace.mission.controller.response.MissionResponse;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final FavoriteMissionRepository favoriteMissionRepository;
    private final WorkerRepository workerRepository;
    private final WorkspaceRepository workspaceRepository;

    public List<MissionResponse> getMissionsInWorkspace(User loginedUser, Long workspaceId) {
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspaceId);
        List<Mission> missions = missionRepository.getAllByWorkspaceId(workspaceId);
        List<Mission> favoriteMissions = favoriteMissionRepository.getAllByWorkerId(worker.getId()).stream()
                .map(favoriteMission -> favoriteMission.getMission())
                .toList();

        List<MissionResponse> responses = new ArrayList<>();
        for (Mission mission : missions) {
            boolean isFavorite = favoriteMissions.contains(mission);
            responses.add(new MissionResponse(mission, isFavorite));
        }
        return responses;
    }

    private Worker validateIfWorkerIsInWorkspace(Long userId, Long workspaceId) {
        return workerRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotHavePermissionException(ErrorCode.NOT_JOINED_WORKSPACE));
    }


    @Transactional
    public void toggleRegistrationOfFavoriteMission(User loginedUser, Long workspaceId, Long missionId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspace.getId());
        Mission mission = missionRepository.findInWorkspace(workspace.getId(), missionId);

        Optional<FavoriteMission> favoriteMission = favoriteMissionRepository.findByWorkerIdAndMissionId(worker.getId(), missionId);
        favoriteMission.ifPresentOrElse(
                fm -> favoriteMissionRepository.deleteById(fm.getId()),
                () -> favoriteMissionRepository.save(new FavoriteMission(worker, mission))
        );
    }

    public List<FavoriteMissionResponse> getFavoriteMissions(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = validateIfWorkerIsInWorkspace(loginedUser.getId(), workspace.getId());
        List<FavoriteMission> favoriteMissions = favoriteMissionRepository.getAllByWorkerId(worker.getId());
        return favoriteMissions.stream()
                .map(FavoriteMission::getMission)
                .map(FavoriteMissionResponse::new)
                .toList();
    }

}
