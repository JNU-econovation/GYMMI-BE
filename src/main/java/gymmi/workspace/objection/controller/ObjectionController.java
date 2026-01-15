package gymmi.workspace.objection.controller;

import gymmi.user.domain.User;
import gymmi.global.common.resolver.Logined;
import gymmi.workspace.objection.domain.ObjectionStatus;
import gymmi.workspace.objection.service.ObjectionService;
import gymmi.workspace.objection.controller.request.ObjectionRequest;
import gymmi.workspace.objection.controller.response.ObjectionAlarmResponse;
import gymmi.workspace.objection.controller.response.ObjectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ObjectionController {

    private final ObjectionService objectionService;

    @PostMapping("/workspaces/{workspaceId}/workout-confirmations/{workoutConfirmationId}")
    public ResponseEntity<Void> objectToWorkoutConfirmation(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long workoutConfirmationId,
            @Validated @RequestBody ObjectionRequest request
    ) {
        objectionService.objectToWorkoutHistory(user, workspaceId, workoutConfirmationId, request.getReason());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/workspaces/{workspaceId}/objections")
    public ResponseEntity<List<ObjectionAlarmResponse>> terminateObjections(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
//        workspaceCommandService.terminateExpiredObjection(user, workspaceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workspaces/{workspaceId}/objections")
    public ResponseEntity<List<ObjectionAlarmResponse>> seeObjections(
            @Logined User user,
            @PathVariable Long workspaceId,
            @RequestParam int pageNumber,
            @RequestParam("status") ObjectionStatus objectionStatus
    ) {
        List<ObjectionAlarmResponse> responses = objectionService.getObjections(user, workspaceId, pageNumber, objectionStatus);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/workspaces/{workspaceId}/objections/{objectionId}")
    public ResponseEntity<ObjectionResponse> seeObjection(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long objectionId
    ) {
        ObjectionResponse response = objectionService.getObjection(user, workspaceId, objectionId);
        return ResponseEntity.ok().body(response);
    }

}
