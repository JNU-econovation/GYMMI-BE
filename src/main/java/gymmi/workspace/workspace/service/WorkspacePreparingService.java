package gymmi.workspace.workspace.service;

import gymmi.etc.domain.entity.User;
import gymmi.global.eventlistener.event.WorkspaceStartedEvent;
import gymmi.workspace.mission.domain.Missions;
import gymmi.workspace.mission.repository.FavoriteMissionRepository;
import gymmi.workspace.mission.repository.MissionRepository;
import gymmi.workspace.workspace.controller.request.CreatingWorkspaceRequest;
import gymmi.workspace.workspace.domain.*;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.workspace.repository.WorkerRepository;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspacePreparingService {

    private final WorkspaceRepository workspaceRepository;
    private final MissionRepository missionRepository;
    private final WorkerRepository workerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final FavoriteMissionRepository favoriteMissionRepository;

    @Transactional
    // 중복 요청
    public Long setUpWorkspace(User loginedUser, CreatingWorkspaceRequest request) {
        boolean isExist = workspaceRepository.existsByName(request.getName());
        WorkspaceCreationValidator.validateDuplicateName(isExist);

        int count = workspaceRepository.countsActivateWorkspace(loginedUser.getId());
        WorkspaceJoinValidator.validateWorkspaceCountLimit(count);

        Workspace workspace = WorkspaceRequestMapper.createFrom(loginedUser, request);
        workspaceRepository.save(workspace);

        Missions missions = WorkspaceRequestMapper.createFrom(workspace, request.getMissionBoard());
        missionRepository.saveAll(missions.getMissions());


        Worker worker = new Worker(loginedUser, workspace);
        workerRepository.save(worker);

        return workspace.getId();
    }


    @Transactional
    // 동시 참여 -> 인원수 초과, 중복 요청 -> 중복 참여자 존재
    public void joinWorkspace(User loginedUser, Long workspaceId, String workspacePassword) {
        int count = workspaceRepository.countsActivateWorkspace(loginedUser.getId());
        WorkspaceJoinValidator.validateWorkspaceCountLimit(count);

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
