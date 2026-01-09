package gymmi.workspace.service;

import gymmi.entity.User;
import gymmi.eventlistener.event.WorkspaceStartedEvent;
import gymmi.workspace.service.domain.workspace.LeftWorker;
import gymmi.workspace.service.domain.mission.Missions;
import gymmi.workspace.service.domain.workspace.WorkspacePreparingManager;
import gymmi.workspace.service.domain.workspace.Worker;
import gymmi.workspace.service.domain.workspace.Workspace;
import gymmi.workspace.service.domain.workspace.WorkspaceCreationValidator;
import gymmi.workspace.service.domain.workspace.WorkspaceJoinValidator;
import gymmi.workspace.service.domain.workspace.WorkspaceStarter;
import gymmi.workspace.repository.FavoriteMissionRepository;
import gymmi.workspace.repository.MissionRepository;
import gymmi.workspace.repository.WorkerRepository;
import gymmi.workspace.repository.WorkspaceRepository;
import gymmi.workspace.request.CreatingWorkspaceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspacePreparingService {

    private final WorkspaceCreationValidator workspaceCreationValidator;
    private final WorkspaceJoinValidator workspaceJoinValidator;
    private final WorkspaceRepository workspaceRepository;
    private final MissionRepository missionRepository;
    private final WorkerRepository workerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final FavoriteMissionRepository favoriteMissionRepository;

    @Transactional
    // 중복 요청
    public Long setUpWorkspace(User loginedUser, CreatingWorkspaceRequest request) {
        workspaceCreationValidator.validateDuplicateName(request.getName());
        Workspace workspace = WorkspaceRequestMapper.createFrom(loginedUser, request);
        workspaceRepository.save(workspace);

        Missions missions = WorkspaceRequestMapper.createFrom(workspace, request.getMissionBoard());
        missionRepository.saveAll(missions.getMissions());

        workspaceJoinValidator.validateWorkspaceCountLimit(loginedUser.getId());
        Worker worker = new Worker(loginedUser, workspace);
        workerRepository.save(worker);

        return workspace.getId();
    }


    @Transactional
    // 동시 참여 -> 인원수 초과, 중복 요청 -> 중복 참여자 존재
    public void joinWorkspace(User loginedUser, Long workspaceId, String workspacePassword) {
        workspaceJoinValidator.validateWorkspaceCountLimit(loginedUser.getId());

        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());

        WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);
        Worker worker = workspacePreparingManager.allow(loginedUser, workspacePassword);

        workerRepository.save(worker);
    }


    @Transactional
    public void startWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspace.getId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());

        WorkspaceStarter workspaceStarter = new WorkspaceStarter(workspace, workers);
        Workspace startedWorkspace = workspaceStarter.startBy(worker);

        workspaceRepository.save(startedWorkspace);
        applicationEventPublisher.publishEvent(new WorkspaceStartedEvent(workspace.getId()));
    }

    @Transactional
    public void leaveWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspace.getId());
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());

        WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, workers);
        LeftWorker leftWorker = workspacePreparingManager.release(worker);

        cleanUpWorker(leftWorker);

        if (leftWorker.isLastLeaver()) {
            cleanUpWorkspace(workspaceId);
        }
    }

    private void cleanUpWorkspace(Long workspaceId) {
        missionRepository.deleteAllByWorkspaceId(workspaceId);
        workspaceRepository.deleteById(workspaceId);
    }

    private void cleanUpWorker(LeftWorker leftWorker) {
        favoriteMissionRepository.deleteAllByWorkerId(leftWorker.getWorker().getId());
        workerRepository.delete(leftWorker.getWorker());
    }
}
