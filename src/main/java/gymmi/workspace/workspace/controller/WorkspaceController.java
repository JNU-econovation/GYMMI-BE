package gymmi.workspace.workspace.controller;

import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.workspace.workspace.controller.request.EditingIntroductionOfWorkspaceRequest;
import gymmi.workspace.workspace.controller.request.MatchingWorkspacePasswordRequest;
import gymmi.workspace.workspace.controller.response.*;
import gymmi.workspace.workspace.domain.WorkspaceStatus;
import gymmi.workspace.workspace.service.WorkspaceProgressService;
import gymmi.workspace.workspace.service.WorkspaceCommonService;
import gymmi.workspace.workspace.service.WorkspaceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceProgressService workspaceProgressService;
    private final WorkspaceQueryService workspaceQueryService;
    private final WorkspaceCommonService workspaceCommonService;

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

    @GetMapping("/workspaces/{workspaceId}")
    public ResponseEntity<InsideWorkspaceResponse> enterWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        InsideWorkspaceResponse response = workspaceQueryService.enterWorkspace(user, workspaceId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/workspaces/{workspaceId}/edit")
    public ResponseEntity<Void> editDescriptionOfWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId,
            @RequestBody @Validated EditingIntroductionOfWorkspaceRequest request
    ) {
        workspaceCommonService.editIntroduction(user, workspaceId, request);
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

    @GetMapping("/workspaces/{workspaceId}/result")
    public ResponseEntity<WorkspaceResultResponse> readWorkspaceResult(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        WorkspaceResultResponse response = workspaceProgressService.getWorkspaceResult(user, workspaceId);
        return ResponseEntity.ok().body(response);
    }

}
