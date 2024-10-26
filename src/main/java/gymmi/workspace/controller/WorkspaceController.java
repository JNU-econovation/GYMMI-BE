package gymmi.workspace.controller;

import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.response.IdResponse;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.request.CreatingWorkspaceRequest;
import gymmi.workspace.request.EditingIntroductionOfWorkspaceRequest;
import gymmi.workspace.request.JoiningWorkspaceRequest;
import gymmi.workspace.request.MatchingWorkspacePasswordRequest;
import gymmi.workspace.request.WorkingMissionInWorkspaceRequest;
import gymmi.workspace.response.CheckingCreationOfWorkspaceResponse;
import gymmi.workspace.response.CheckingEntranceOfWorkspaceResponse;
import gymmi.workspace.response.InsideWorkspaceResponse;
import gymmi.workspace.response.JoinedWorkspaceResponse;
import gymmi.workspace.response.MatchingWorkspacePasswordResponse;
import gymmi.workspace.response.MissionResponse;
import gymmi.workspace.response.OpeningTasksBoxResponse;
import gymmi.workspace.response.WorkingScoreResponse;
import gymmi.workspace.response.WorkoutContextResponse;
import gymmi.workspace.response.WorkoutRecordResponse;
import gymmi.workspace.response.WorkspaceIntroductionResponse;
import gymmi.workspace.response.WorkspaceResponse;
import gymmi.workspace.service.WorkspaceCommandService;
import gymmi.workspace.service.WorkspaceQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceCommandService workspaceCommandService;
    private final WorkspaceQueryService workspaceQueryService;

    @PostMapping("/workspaces")
    public ResponseEntity<IdResponse> createWorkspace(
            @Logined User user,
            @Validated @RequestBody CreatingWorkspaceRequest request
    ) {
        Long workspaceId = workspaceCommandService.createWorkspace(user, request);
        return ResponseEntity.ok().body(new IdResponse(workspaceId));
    }

    @PostMapping("/workspaces/{workspaceId}/join")
    public ResponseEntity<Void> joinWorkspace(
            @Logined User user,
            @Validated @RequestBody JoiningWorkspaceRequest request,
            @PathVariable Long workspaceId
    ) {
        workspaceCommandService.joinWorkspace(user, workspaceId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workspaces/{workspaceId}/introduction")
    public ResponseEntity<WorkspaceIntroductionResponse> seeWorkspaceIntroduction(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        WorkspaceIntroductionResponse response = workspaceQueryService.getWorkspaceIntroduction(user, workspaceId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/workspaces/{workspaceId}/match-password")
    public ResponseEntity<MatchingWorkspacePasswordResponse> matchWorkspacePassword(
            @Logined User user,
            @PathVariable Long workspaceId,
            @Validated @RequestBody MatchingWorkspacePasswordRequest request
    ) {
        MatchingWorkspacePasswordResponse response = workspaceQueryService.matchesWorkspacePassword(workspaceId,
                request.getPassword());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/my")
    public ResponseEntity<List<JoinedWorkspaceResponse>> seeJoinedWorkspaces(
            @Logined User user,
            @RequestParam("page") int pageNumber
    ) {
        List<JoinedWorkspaceResponse> responses = workspaceQueryService.getJoinedAllWorkspaces(user, pageNumber);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/workspaces")
    public ResponseEntity<List<WorkspaceResponse>> seeAllWorkspaces(
            @Logined User user,
            @RequestParam(required = false) WorkspaceStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(value = "page") int pageNumber
    ) {
        List<WorkspaceResponse> responses = workspaceQueryService.getAllWorkspaces(status, keyword, pageNumber);
        return ResponseEntity.ok().body(responses);
    }

    @PatchMapping("/workspaces/{workspaceId}/start")
    public ResponseEntity<Void> startWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        workspaceCommandService.startWorkspace(user, workspaceId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/workspaces/{workspaceId}/leave")
    public ResponseEntity<Void> leaveWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        workspaceCommandService.leaveWorkspace(user, workspaceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<InsideWorkspaceResponse> enterWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        InsideWorkspaceResponse response = workspaceQueryService.enterWorkspace(user, workspaceId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/{workspaceId}/missions")
    public ResponseEntity<List<MissionResponse>> seeMissionsInWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        List<MissionResponse> responses = workspaceQueryService.getMissionsInWorkspace(user, workspaceId);
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("/workspaces/{workspaceId}/missions")
    public ResponseEntity<WorkingScoreResponse> workMissionsInWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId,
            @RequestBody List<WorkingMissionInWorkspaceRequest> requests
    ) {
        Integer workingScore = workspaceCommandService.workMissionsInWorkspace(user, workspaceId, requests);
        return ResponseEntity.ok().body(new WorkingScoreResponse(workingScore));
    }

    @GetMapping("/workspaces/{workspaceId}/workout-context/{userId}")
    public ResponseEntity<WorkoutContextResponse> seeWorkoutContextInWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long userId
    ) {
        WorkoutContextResponse response = workspaceQueryService.getWorkoutContext(user, workspaceId, userId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/{workspaceId}/workout-histories/{userId}/{workoutHistoryId}")
    public ResponseEntity<List<WorkoutRecordResponse>> seeWorkoutRecordsOfWorkoutHistory(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long workoutHistoryId
    ) {
        List<WorkoutRecordResponse> response = workspaceQueryService.getWorkoutRecordsInWorkoutHistory(
                user, workspaceId, workoutHistoryId
        );
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/workspaces/{workspaceId}/tasks")
    public ResponseEntity<OpeningTasksBoxResponse> openTasksBoxInWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        OpeningTasksBoxResponse response = workspaceCommandService.openTaskBoxInWorkspace(user, workspaceId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/workspaces/{workspaceId}/edit")
    public ResponseEntity<Void> editDescriptionOfWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId,
            @RequestBody @Validated EditingIntroductionOfWorkspaceRequest request
    ) {
        workspaceCommandService.editIntroduction(user, workspaceId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workspaces/{workspaceId}/enter")
    public ResponseEntity<CheckingEntranceOfWorkspaceResponse> checkEntrance(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        CheckingEntranceOfWorkspaceResponse response = workspaceQueryService.checkEnteringWorkspace(user,
                workspaceId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/check-creation")
    public ResponseEntity<CheckingCreationOfWorkspaceResponse> checkCreatingOfWorkspace(
            @Logined User user
    ) {
        CheckingCreationOfWorkspaceResponse response = workspaceQueryService.checkCreatingOfWorkspace(user);
        return ResponseEntity.ok().body(response);
    }
}
