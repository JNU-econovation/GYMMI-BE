package gymmi.workspace.workout.controller;

import gymmi.user.domain.User;
import gymmi.global.common.resolver.Logined;
import gymmi.workspace.workout.controller.request.WorkoutRequest;
import gymmi.workspace.workout.controller.response.*;
import gymmi.workspace.workout.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping("/workspaces/{workspaceId}/missions")
    public ResponseEntity<WorkingScoreResponse> workMissionsInWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId,
            @Validated @RequestBody WorkoutRequest request
    ) {
        Integer workingScore = workoutService.workMissionsInWorkspace(user, workspaceId, request);
        return ResponseEntity.ok().body(new WorkingScoreResponse(workingScore));
    }


    @GetMapping("/workspaces/{workspaceId}/workout-confirmations/{workoutConfirmationId}")
    public ResponseEntity<WorkoutConfirmationDetailResponse> seeWorkoutConfirmation(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long workoutConfirmationId
    ) {
        WorkoutConfirmationDetailResponse response = workoutService.getWorkoutConfirmation(user, workspaceId, workoutConfirmationId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/{workspaceId}/workout-confirmations")
    public ResponseEntity<WorkoutConfirmationResponse> seeWorkoutConfirmations(
            @Logined User user,
            @PathVariable Long workspaceId,
            @RequestParam int pageNumber
    ) {
        WorkoutConfirmationResponse response = workoutService.getWorkoutConfirmations(user, workspaceId, pageNumber);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/{workspaceId}/workout-histories/{userId}/{workoutHistoryId}")
    public ResponseEntity<List<WorkoutRecordResponse>> seeWorkoutRecordsOfWorkoutHistory(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long workoutHistoryId
    ) {
        List<WorkoutRecordResponse> response = workoutService.getWorkoutRecordsInWorkoutHistory(
                user, workspaceId, workoutHistoryId
        );
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/workspaces/{workspaceId}/workout-context/{userId}")
    public ResponseEntity<WorkoutContextResponse> seeWorkoutContextInWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long userId
    ) {
        WorkoutContextResponse response = workoutService.getWorkoutContext(user, workspaceId, userId);
        return ResponseEntity.ok().body(response);
    }
}
