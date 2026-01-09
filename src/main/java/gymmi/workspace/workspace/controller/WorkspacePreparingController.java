package gymmi.workspace.workspace.controller;

import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.response.IdResponse;
import gymmi.workspace.workspace.controller.request.CreatingWorkspaceRequest;
import gymmi.workspace.workspace.controller.request.JoiningWorkspaceRequest;
import gymmi.workspace.workspace.service.WorkspacePreparingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WorkspacePreparingController {

    private final WorkspacePreparingService workspacePreparingService;

    @PostMapping("/workspaces")
    public ResponseEntity<IdResponse> createWorkspace(
            @Logined User user,
            @Validated @RequestBody CreatingWorkspaceRequest request
    ) {
        Long workspaceId = workspacePreparingService.setUpWorkspace(user, request);
        return ResponseEntity.ok().body(new IdResponse(workspaceId));
    }

    @PostMapping("/workspaces/{workspaceId}/join")
    public ResponseEntity<Void> joinWorkspace(
            @Logined User user,
            @Validated @RequestBody JoiningWorkspaceRequest request,
            @PathVariable Long workspaceId
    ) {
        workspacePreparingService.joinWorkspace(user, workspaceId, request.getPassword());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/workspaces/{workspaceId}/start")
    public ResponseEntity<Void> startWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        workspacePreparingService.startWorkspace(user, workspaceId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/workspaces/{workspaceId}/leave")
    public ResponseEntity<Void> leaveWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        workspacePreparingService.leaveWorkspace(user, workspaceId);
        return ResponseEntity.ok().build();
    }
}
