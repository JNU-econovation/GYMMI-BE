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
import gymmi.response.JoinedWorkspaceResponse;
import gymmi.response.MatchingWorkspacePasswordResponse;
import gymmi.response.WorkspacePasswordResponse;
import gymmi.response.WorkspaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        if (!workspace.matchesPassword(request.getPassword())) {
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

        int workerCount = workerRepository.countAllByWorkspaceId(workspace.getId());
        if (workspace.isFull(workerCount)) {
            throw new InvalidStateException("워크스페이스 인원이 가득 찼습니다.");
            // 동시에 참여하는 경우?? 검증로직 피할수도? 락사용
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
        validateIfWorker(loginedUser.getId(), workspaceId);
        return new WorkspacePasswordResponse(workspace.getPassword());
    }

    private void validateIfWorker(Long userId, Long workspaceId) {
        if (workerRepository.findByUserIdAndWorkspaceId(userId, workspaceId).isEmpty()) {
            throw new NotHavePermissionException("해당 워크스페이스의 참여자가 아닙니다.");
        }
    }

    public MatchingWorkspacePasswordResponse matchesWorkspacePassword(Long workspaceId, String workspacePassword) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        boolean matchingResult = workspace.matchesPassword(workspacePassword);
        return new MatchingWorkspacePasswordResponse(matchingResult);
    }

    public List<JoinedWorkspaceResponse> getJoinedWorkspaces(User loginedUser, int pageNumber) {
        List<Workspace> joinedWorkspaces = workspaceRepository.getJoinedWorkspacesByUserId(loginedUser.getId(), pageNumber);
        List<JoinedWorkspaceResponse> responses = new ArrayList<>();
        for (Workspace workspace : joinedWorkspaces) {
            Integer achievementScore = workspaceRepository.getAchievementScore(workspace.getId());
            JoinedWorkspaceResponse response = JoinedWorkspaceResponse.builder()
                    .id(workspace.getId())
                    .name(workspace.getName())
                    .creator(workspace.getCreator().getNickname())
                    .status(workspace.getStatus().name())
                    .tag(workspace.getTag())
                    .createdAt(workspace.getCreatedAt())
                    .goalScore(workspace.getGoalScore())
                    .achievementScore(achievementScore)
                    .build();
            responses.add(response);
        }
        return responses;
    }

    public List<WorkspaceResponse> getAllWorkspaces() {
        List<Workspace> workspaces = workspaceRepository.getAllWorkspaces();
        List<WorkspaceResponse> responses = new ArrayList<>();
        for (Workspace workspace : workspaces) {
            Integer achievementScore = workspaceRepository.getAchievementScore(workspace.getId());
            WorkspaceResponse response = WorkspaceResponse.builder()
                    .id(workspace.getId())
                    .name(workspace.getName())
                    .status(workspace.getStatus().name())
                    .goalScore(workspace.getGoalScore())
                    .createdAt(workspace.getCreatedAt())
                    .achievementScore(achievementScore)
                    .build();
            responses.add(response);
        }
        return responses;
    }

    @Transactional
    public void startWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        validateIfWorker(loginedUser.getId(), workspaceId);
        if (!workspace.isCreator(loginedUser)) {
            throw new NotHavePermissionException("방장이 아닙니다.");
        }
        int workerCount = workerRepository.countAllByWorkspaceId(workspace.getId());
        if (workerCount < 2) {
            throw new InvalidStateException("최소 인원인 2명을 채워주세요.");
        }
        workspace.start();
    }

    @Transactional
    public void leaveWorkspace(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.getWorkspaceById(workspaceId);
        if (!workspace.isPreparing()) {
            throw new InvalidStateException("준비 단계에서만 나갈 수 있습니다.");
        }

        if (workspace.isCreator(loginedUser)) {
            int workerCount = workerRepository.countAllByWorkspaceId(workspace.getId());
            if (workerCount != 1) {
                throw new InvalidStateException("방장 이외에 참여자가 존재합니다.");
            }
            deleteTaskAndWorker(loginedUser, workspaceId);
            deleteMissionsAndWorkspace(workspaceId, workspace);
            return;
        }

        deleteTaskAndWorker(loginedUser, workspaceId);
    }

    private void deleteMissionsAndWorkspace(Long workspaceId, Workspace workspace) {
        missionRepository.deleteAllByWorkspaceId(workspace.getId());
        workspaceRepository.deleteById(workspaceId);
    }

    private void deleteTaskAndWorker(User loginedUser, Long workspaceId) {
        taskRepository.deleteByUserIdAndWorkspaceId(loginedUser.getId(), workspaceId);
        workerRepository.deleteByUserIdAndWorkspaceId(loginedUser.getId(), workspaceId);
    }
}

