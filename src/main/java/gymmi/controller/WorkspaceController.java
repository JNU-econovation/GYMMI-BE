package gymmi.controller;

import gymmi.entity.User;
import gymmi.entity.WorkspaceStatus;
import gymmi.global.Logined;
import gymmi.request.*;
import gymmi.response.*;
import gymmi.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping("/workspaces")
    public ResponseEntity<IdResponse> createWorkspace(
            @Logined User user,
            @Validated @RequestBody CreatingWorkspaceRequest request
    ) {
        Long workspaceId = workspaceService.createWorkspace(user, request);
        return ResponseEntity.ok().body(new IdResponse(workspaceId));
    }

    @PostMapping("/workspaces/{workspaceId}/join")
    public ResponseEntity<Void> joinWorkspace(
            @Logined User user,
            @Validated @RequestBody JoiningWorkspaceRequest request,
            @PathVariable Long workspaceId
    ) {
        workspaceService.joinWorkspace(user, workspaceId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workspaces/{workspaceId}/introduction")
    public ResponseEntity<WorkspaceIntroductionResponse> seeWorkspaceIntroduction(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        WorkspaceIntroductionResponse response = workspaceService.getWorkspaceIntroduction(user, workspaceId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/workspaces/{workspaceId}/match-password")
    public ResponseEntity<MatchingWorkspacePasswordResponse> matchWorkspacePassword(
            @Logined User user,
            @PathVariable Long workspaceId,
            @Validated @RequestBody MatchingWorkspacePasswordRequest request
    ) {
        MatchingWorkspacePasswordResponse response = workspaceService.matchesWorkspacePassword(workspaceId,
                request.getPassword());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/my")
    public ResponseEntity<List<JoinedWorkspaceResponse>> seeJoinedWorkspaces(
            @Logined User user,
            @RequestParam("page") int pageNumber
    ) {
        List<JoinedWorkspaceResponse> responses = workspaceService.getJoinedAllWorkspaces(user, pageNumber);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/workspaces")
    public ResponseEntity<List<WorkspaceResponse>> seeAllWorkspaces(
            @Logined User user,
            @RequestParam(required = false) WorkspaceStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(value = "page") int pageNumber
    ) {
        List<WorkspaceResponse> responses = workspaceService.getAllWorkspaces(status, keyword, pageNumber);
        return ResponseEntity.ok().body(responses);
    }

    @PatchMapping("/workspaces/{workspaceId}/start")
    public ResponseEntity<Void> startWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        workspaceService.startWorkspace(user, workspaceId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/workspaces/{workspaceId}/leave")
    public ResponseEntity<Void> leaveWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        workspaceService.leaveWorkspace(user, workspaceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<InsideWorkspaceResponse> enterWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        InsideWorkspaceResponse response = workspaceService.enterWorkspace(user, workspaceId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/{workspaceId}/missions")
    public ResponseEntity<List<MissionResponse>> seeMissionsInWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        List<MissionResponse> responses = workspaceService.getMissionsInWorkspace(user, workspaceId);
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("/workspaces/{workspaceId}/missions")
    public ResponseEntity<WorkingScoreResponse> workMissionsInWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId,
            @RequestBody List<WorkingMissionInWorkspaceRequest> requests
    ) {
        Integer workingScore = workspaceService.workMissionsInWorkspace(user, workspaceId, requests);
        return ResponseEntity.ok().body(new WorkingScoreResponse(workingScore));
    }

    @GetMapping("/workspaces/{workspaceId}/workings/{userId}")
    public ResponseEntity<List<ContributedWorkingResponse>> seeSumOfWorkingsInWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long userId
    ) {
        List<ContributedWorkingResponse> responses =
                workspaceService.getContributedWorkoutOfWorkerInWorkspace(user, workspaceId, userId);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/workspaces/{workspaceId}/tasks")
    public ResponseEntity<OpeningTasksBoxResponse> openTasksBoxInWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        OpeningTasksBoxResponse response = workspaceService.openTaskBoxInWorkspace(user, workspaceId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/workspaces/{workspaceId}/edit")
    public ResponseEntity<Void> editDescriptionOfWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId,
            @RequestBody @Validated EditingIntroductionOfWorkspaceRequest request
    ) {
        workspaceService.editIntroduction(user, workspaceId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workspaces/{workspaceId}/enter")
    public ResponseEntity<CheckingEntranceOfWorkspaceResponse> checkEntrance(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        CheckingEntranceOfWorkspaceResponse response = workspaceService.checkEnteringWorkspace(user, workspaceId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/check-creation")
    public ResponseEntity<CheckingCreationOfWorkspaceResponse> checkCreatingOfWorkspace(
            @Logined User user
    ) {
        CheckingCreationOfWorkspaceResponse response = workspaceService.checkCreatingOfWorkspace(user);
        return ResponseEntity.ok().body(response);
    }
}
