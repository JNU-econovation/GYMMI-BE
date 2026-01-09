package gymmi.workspace.controller;

import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.workspace.service.domain.objection.ObjectionStatus;
import gymmi.workspace.service.domain.workspace.WorkspaceStatus;
import gymmi.workspace.request.*;
import gymmi.workspace.response.*;
import gymmi.workspace.service.WorkspaceProgressService;
import gymmi.workspace.service.WorkspaceCommonService;
import gymmi.workspace.service.WorkspaceQueryService;
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
            @Validated @RequestBody WorkoutRequest request
    ) {
        Integer workingScore = workspaceProgressService.workMissionsInWorkspace(user, workspaceId, request);
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

    @PostMapping("/workspace/{workspaceId}/missions/{missionId}")
    public ResponseEntity<Void> toggleRegistrationOfFavoriteMission(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long missionId
    ) {
        workspaceCommonService.toggleRegistrationOfFavoriteMission(user, workspaceId, missionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workspace/{workspaceId}/missions/favorite")
    public ResponseEntity<List<FavoriteMissionResponse>> seeFavoriteMissions(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        List<FavoriteMissionResponse> responses = workspaceQueryService.getFavoriteMissions(user, workspaceId);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/workspaces/{workspaceId}/workout-confirmations")
    public ResponseEntity<WorkoutConfirmationResponse> seeWorkoutConfirmations(
            @Logined User user,
            @PathVariable Long workspaceId,
            @RequestParam int pageNumber
    ) {
        WorkoutConfirmationResponse response = workspaceQueryService.getWorkoutConfirmations(user, workspaceId, pageNumber);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/{workspaceId}/workout-confirmations/{workoutConfirmationId}")
    public ResponseEntity<WorkoutConfirmationDetailResponse> seeWorkoutConfirmation(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long workoutConfirmationId
    ) {
        WorkoutConfirmationDetailResponse response = workspaceQueryService.getWorkoutConfirmation(user, workspaceId, workoutConfirmationId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/workspaces/{workspaceId}/workout-confirmations/{workoutConfirmationId}")
    public ResponseEntity<Void> objectToWorkoutConfirmation(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long workoutConfirmationId,
            @Validated @RequestBody ObjectionRequest request
    ) {
        workspaceProgressService.objectToWorkoutHistory(user, workspaceId, workoutConfirmationId, request.getReason());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/workspaces/{workspaceId}/objections/{objectionId}")
    public ResponseEntity<Void> voteToObjection(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long objectionId,
            @Validated @RequestBody VoteRequest request
    ) {
        workspaceProgressService.voteToObjection(user, workspaceId, objectionId, request.getWillApprove());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workspaces/{workspaceId}/objections/{objectionId}")
    public ResponseEntity<ObjectionResponse> seeObjection(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long objectionId
    ) {
        ObjectionResponse response = workspaceQueryService.getObjection(user, workspaceId, objectionId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/workspaces/{workspaceId}/objections")
    public ResponseEntity<List<ObjectionAlarmResponse>> seeObjections(
            @Logined User user,
            @PathVariable Long workspaceId,
            @RequestParam int pageNumber,
            @RequestParam("status") ObjectionStatus objectionStatus
    ) {
        List<ObjectionAlarmResponse> responses = workspaceQueryService.getObjections(user, workspaceId, pageNumber, objectionStatus);
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("/workspaces/{workspaceId}/objections")
    public ResponseEntity<List<ObjectionAlarmResponse>> terminateObjections(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
//        workspaceCommandService.terminateExpiredObjection(user, workspaceId);
        return ResponseEntity.ok().build();
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
