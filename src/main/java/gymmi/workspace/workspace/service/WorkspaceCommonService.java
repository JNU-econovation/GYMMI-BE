package gymmi.workspace.workspace.service;

import gymmi.entity.User;
import gymmi.workspace.workspace.domain.WorkspaceEditManager;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.mission.repository.FavoriteMissionRepository;
import gymmi.workspace.mission.repository.MissionRepository;
import gymmi.workspace.workspace.repository.WorkerRepository;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import gymmi.workspace.workspace.controller.request.EditingIntroductionOfWorkspaceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
