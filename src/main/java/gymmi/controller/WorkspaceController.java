package gymmi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkspaceController {

    @PostMapping("/workspaces")
    public ResponseEntity<Void> createWorkspace() {
        return null;
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
