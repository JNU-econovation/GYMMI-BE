package gymmi.service;

import gymmi.entity.*;
import gymmi.exception.AlreadyExistException;
import gymmi.exception.InvalidStateException;
import gymmi.exception.NotHavePermissionException;
import gymmi.exception.NotMatchedException;
import gymmi.repository.MissionRepository;
import gymmi.repository.TaskRepository;
import gymmi.repository.WorkerRepository;
import gymmi.repository.WorkspaceRepository;
import gymmi.request.CreatingWorkspaceRequest;
import gymmi.request.JoiningWorkspaceRequest;
import gymmi.request.MissionDTO;
import gymmi.response.WorkspacePasswordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final MissionRepository missionRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public Long createWorkspace(User loginedUser, CreatingWorkspaceRequest request) {
        if (workspaceRepository.findWorkspaceByByName(request.getName()).isPresent()) {
            throw new AlreadyExistException("이미 존재하는 워크스페이스 이름 입니다.");
        }

        Workspace workspace = Workspace.builder()
                .creator(loginedUser)
                .name(request.getName())
                .headCount(request.getHeadCount())
                .goalScore(request.getGoalScore())
                .description(request.getDescription())
                .tag(request.getTag())
                .build();
        Workspace savedWorkspace = workspaceRepository.save(workspace);

        List<MissionDTO> missionBoard = request.getMissionBoard();
        for (MissionDTO missionDTO : missionBoard) {
            Mission mission = Mission.builder()
                    .workspace(savedWorkspace)
                    .name(missionDTO.getMission())
                    .score(missionDTO.getScore())
                    .build();
            missionRepository.save(mission);
        }

        setTask(loginedUser, savedWorkspace, request.getTask());
        enterWorkspace(loginedUser, savedWorkspace);

        return savedWorkspace.getId();
    }


    @Transactional
    public void joinWorkspace(User loginedUser, Long workspaceId, JoiningWorkspaceRequest request) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        if (!workspace.matches(request.getPassword())) {
            throw new NotMatchedException("비밀번호가 일치하지 않습니다.");
        }
        enterWorkspace(loginedUser, workspace);
        setTask(loginedUser, workspace, request.getTask());
    }

    private void enterWorkspace(User loginedUser, Workspace workspace) {
        if (workerRepository.findByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId()).isPresent()) {
            throw new AlreadyExistException("이미 참여한 워크스페이스 입니다.");
        }

        if (!workspace.isPreparing()) {
            throw new InvalidStateException("준비중인 워크스페이스에만 참여할 수 있습니다.");
        }

        int wokerCount = workerRepository.countAllByWorkspaceId(workspace.getId());
        if (workspace.isFull(wokerCount)) {
            throw new InvalidStateException("워크스페이스 인원이 가득 찼습니다.");
        }

        Worker worker = Worker.builder()
                .workspace(workspace)
                .user(loginedUser)
                .build();
        workerRepository.save(worker);
    }

    private void setTask(User loginedUser, Workspace workspace, String taskName) {
        if (taskRepository.findByUserIdAndWorkspaceId(loginedUser.getId(), workspace.getId()).isPresent()) {
            throw new AlreadyExistException("이미 테스크를 작성하였습니다.");
        }
        Task task = Task.builder()
                .workspace(workspace)
                .user(loginedUser)
                .name(taskName)
                .build();
        taskRepository.save(task);
    }

    public WorkspacePasswordResponse getWorkspacePassword(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        if (workerRepository.findByUserIdAndWorkspaceId(loginedUser.getId(), workspaceId).isEmpty()) {
            throw new NotHavePermissionException("해당 워크스페이스의 참여자가 아닙니다.");
        }
        return new WorkspacePasswordResponse(workspace.getPassword());
    }

}