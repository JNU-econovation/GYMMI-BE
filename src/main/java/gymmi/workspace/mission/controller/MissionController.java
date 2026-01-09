package gymmi.workspace.mission.controller;

import gymmi.etc.domain.entity.User;
import gymmi.global.resolver.Logined;
import gymmi.workspace.mission.service.MissionService;
import gymmi.workspace.mission.controller.response.FavoriteMissionResponse;
import gymmi.workspace.mission.controller.response.MissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/workspaces/{workspaceId}/missions")
    public ResponseEntity<List<MissionResponse>> seeMissionsInWorkspace(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        List<MissionResponse> responses = missionService.getMissionsInWorkspace(user, workspaceId);
        return ResponseEntity.ok().body(responses);
    }


    @PostMapping("/workspace/{workspaceId}/missions/{missionId}")
    public ResponseEntity<Void> toggleRegistrationOfFavoriteMission(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long missionId
    ) {
        missionService.toggleRegistrationOfFavoriteMission(user, workspaceId, missionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/workspace/{workspaceId}/missions/favorite")
    public ResponseEntity<List<FavoriteMissionResponse>> seeFavoriteMissions(
            @Logined User user,
            @PathVariable Long workspaceId
    ) {
        List<FavoriteMissionResponse> responses = missionService.getFavoriteMissions(user, workspaceId);
        return ResponseEntity.ok().body(responses);
    }
}
