package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.workspace.service.domain.workspace.WorkspaceEditManager;
import gymmi.workspace.service.domain.mission.FavoriteMission;
import gymmi.workspace.service.domain.mission.Mission;
import gymmi.workspace.service.domain.workspace.Worker;
import gymmi.workspace.service.domain.workspace.Workspace;
import gymmi.workspace.repository.FavoriteMissionRepository;
import gymmi.workspace.repository.MissionRepository;
import gymmi.workspace.repository.WorkerRepository;
import gymmi.workspace.repository.WorkspaceRepository;
import gymmi.workspace.request.EditingIntroductionOfWorkspaceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkspaceCommonService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final MissionRepository missionRepository;
    private final FavoriteMissionRepository favoriteMissionRepository;

    @Transactional
    public void editIntroduction(
            User loginedUser,
            Long workspaceId,
            EditingIntroductionOfWorkspaceRequest request
    ) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspace.getId());

        WorkspaceEditManager workspaceEditManager = new WorkspaceEditManager(workspace, worker);
        workspaceEditManager.edit(request.getDescription(), request.getTag(), request.getTask());
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

}
