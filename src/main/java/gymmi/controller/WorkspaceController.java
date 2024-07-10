package gymmi.controller;

import gymmi.entity.User;
import gymmi.entity.WorkspaceStatus;
import gymmi.global.Logined;
import gymmi.request.CreatingWorkspaceRequest;
import gymmi.request.EditingIntroductionOfWorkspaceRequest;
import gymmi.request.JoiningWorkspaceRequest;
import gymmi.request.MatchingWorkspacePasswordRequest;
import gymmi.request.WorkingMissionInWorkspaceRequest;
import gymmi.response.*;
import gymmi.service.WorkspaceService;
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
        List<JoinedWorkspaceResponse> responses = workspaceService.getJoinedWorkspaces(user, pageNumber);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/workspaces")
    public ResponseEntity<List<WorkspaceResponse>> seeAllWorkspaces(
            @Logined User user,
            @RequestParam(required = false) WorkspaceStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(value = "page") int pageNumber
    ) {
        List<WorkspaceResponse> responses = workspaceService.getAllWorkspaces();
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
                workspaceService.getContributedWorkingsOfWorkerInWorkspace(user, workspaceId, userId);
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

    @GetMapping("/workspaces/{workspaceId}/match-worker")
    public ResponseEntity<MatchingWorkerResponse> matchWorker(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        MatchingWorkerResponse response = workspaceService.matchesWorker(user, workspaceId);
        return ResponseEntity.ok().body(response);
    }
}
