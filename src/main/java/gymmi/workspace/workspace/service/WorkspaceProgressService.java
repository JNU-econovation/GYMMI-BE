package gymmi.workspace.workspace.service;

import gymmi.etc.domain.entity.User;
import gymmi.global.exceptionhandler.exception.InvalidStateException;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.workspace.mission.repository.FavoriteMissionRepository;
import gymmi.workspace.mission.repository.MissionRepository;
import gymmi.workspace.objection.domain.ObjectionAlreadyOpenValidator;
import gymmi.workspace.objection.repository.ObjectionRepository;
import gymmi.workspace.vote.domain.VoteValidator;
import gymmi.workspace.vote.repository.VoteRepository;
import gymmi.workspace.workout.domain.*;
import gymmi.workspace.workout.repository.WorkoutHistoryRepository;
import gymmi.workspace.workout.repository.WorkoutRecordRepository;
import gymmi.workspace.workout.service.WorkoutRequestMapper;
import gymmi.workspace.workspace.domain.WorkspaceDrawManger;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.workspace.domain.entity.WorkspaceResult;
import gymmi.workspace.workspace.controller.response.WorkspaceResultResponse;
import gymmi.workspace.workspace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkspaceProgressService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkerRepository workerRepository;
    private final MissionRepository missionRepository;
    private final WorkoutHistoryRepository workoutHistoryRepository;
    private final FavoriteMissionRepository favoriteMissionRepository;
    private final ObjectionRepository objectionRepository;
    private final VoteRepository voteRepository;
    private final WorkspaceResultRepository workspaceResultRepository;
    private final WorkoutValidator workoutValidator;
    private final WorkoutRequestMapper workoutRequestMapper;
    private final ObjectionAlreadyOpenValidator objectionAlreadyOpenValidator;
    private final VoteValidator voteValidator;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final WorkoutRecordRepository workoutRecordRepository;


    public WorkspaceResultResponse getWorkspaceResult(User loginedUser, Long workspaceId) {
        Workspace workspace = workspaceRepository.findByIdOrThrow(workspaceId);
        Worker worker = workerRepository.findWorkerOrThrow(loginedUser.getId(), workspaceId);
        List<Worker> workers = workerRepository.getAllByWorkspaceId(workspace.getId());
        if (objectionRepository.existsByInProgress(workspace.getId())) {
            throw new InvalidStateException(ErrorCode.EXIST_OBJECTION_IN_PROGRESS);
        }

        if (workspace.isFullyCompleted()) {
            WorkspaceResult workspaceResult = workspaceResultRepository.getByWorkspaceId(workspace.getId());
            return getWorkspaceResultResponse(workspace, workers, workspaceResult);
        }

        WorkspaceDrawManger workspaceDrawManger = new WorkspaceDrawManger(workspace, workers);
        WorkspaceResult workspaceResult = workspaceDrawManger.draw();
        workspaceResult.apply();

        workspaceResultRepository.save(workspaceResult);
        return getWorkspaceResultResponse(workspace, workers, workspaceResult);
    }

    private WorkspaceResultResponse getWorkspaceResultResponse(Workspace workspace, List<Worker> workers, WorkspaceResult workspaceResult) {
        List<Worker> result = sortResult(workspaceResult.getWinner(), workspaceResult.getLoser(), workers);
        return new WorkspaceResultResponse(workspace.getTask(), result);
    }

    private List<Worker> sortResult(Worker winner, Worker loser, List<Worker> workers) {
        LinkedList<Worker> result = new LinkedList<>(workers);
        result.remove(winner);
        result.remove(loser);
        result.addFirst(winner);
        result.addLast(loser);
        return result;
    }
}
