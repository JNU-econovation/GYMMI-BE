package gymmi.controller;

import gymmi.entity.User;
import gymmi.entity.WorkspaceStatus;
import gymmi.global.Logined;
import gymmi.request.CreatingWorkspaceRequest;
import gymmi.request.JoiningWorkspaceRequest;
import gymmi.request.MatchingWorkspacePasswordRequest;
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

    @GetMapping("/workspaces/{workspaceId}/password")
    public ResponseEntity<WorkspacePasswordResponse> seeWorkspacePassword(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        WorkspacePasswordResponse response = workspaceService.getWorkspacePassword(user, workspaceId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/{workspaceId}/match-password")
    public ResponseEntity<MatchingWorkspacePasswordResponse> matchWorkspacePassword(
//            @Logined User user, login intercept 추가
            @PathVariable Long workspaceId,
            @Validated @RequestBody MatchingWorkspacePasswordRequest request
    ) {
        MatchingWorkspacePasswordResponse response = workspaceService.matchesWorkspacePassword(workspaceId, request.getPassword());
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
            //@Logined User user,
            @RequestParam(required = false) WorkspaceStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(value = "page") int pageNumber
    ) {
        List<WorkspaceResponse> responses = workspaceService.getAllWorkspaces();
        return ResponseEntity.ok().body(responses);
    }

    @PatchMapping("/workspaces/{workspaceId}/start")
    public ResponseEntity<Void> startWorkspace() {
        return null;
    }

    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<Void> enterWorkspace() {
        return null;
    }

    @GetMapping("/workspaces/{workspaceId}/missions")
    public ResponseEntity<Void> seeMissionsInWorkspace() {
        return null;
    }

    @PostMapping("/workspaces/{workspaceId}/missions")
    public ResponseEntity<Void> workMissionsInWorkspace() {
        return null;
    }

    @GetMapping("/workspaces/{workspaceId}/tasks")
    public ResponseEntity<Void> openTasksBoxInWorkspace() {
        return null;
    }

}
