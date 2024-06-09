package gymmi.controller;

import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.request.CreatingWorkspaceRequest;
import gymmi.response.IdResponse;
import gymmi.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/workspaces1")
    public ResponseEntity<Void> seeJoinedWorkspaces() {
        return null;
    }

    @GetMapping("/workspaces2")
    public ResponseEntity<Void> seeAllWorkspaces() {
        return null;
    }

    @PostMapping("/workspaces/{workspaceId}/join")
    public ResponseEntity<Void> joinWorkspaces() {
        return null;
    }

    @PostMapping("/workspaces/{workspaceId}/join/task")
    public ResponseEntity<Void> writeTaskInWorkspace() {
        return null;
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
