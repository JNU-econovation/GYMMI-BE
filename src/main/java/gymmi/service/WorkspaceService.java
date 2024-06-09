package gymmi.service;

import gymmi.entity.*;
import gymmi.exception.AlreadyExistException;
import gymmi.repository.MissionRepository;
import gymmi.repository.TaskRepository;
import gymmi.repository.WorkerRepository;
import gymmi.repository.WorkspaceRepository;
import gymmi.request.CreatingWorkspaceRequest;
import gymmi.request.MissionDTO;
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

        Task task = Task.builder()
                .workspace(savedWorkspace)
                .user(loginedUser)
                .name(request.getTask())
                .build();
        taskRepository.save(task);

        Worker worker = Worker.builder()
                .user(loginedUser)
                .workspace(savedWorkspace)
                .build();
        workerRepository.save(worker);

        return savedWorkspace.getId();
    }
}
