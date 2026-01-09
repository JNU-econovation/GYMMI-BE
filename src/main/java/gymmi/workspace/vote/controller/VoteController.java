package gymmi.workspace.vote.controller;

import gymmi.etc.domain.entity.User;
import gymmi.global.resolver.Logined;
import gymmi.workspace.vote.controller.request.VoteRequest;
import gymmi.workspace.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/workspaces/{workspaceId}/objections/{objectionId}")
    public ResponseEntity<Void> voteToObjection(
            @Logined User user,
            @PathVariable Long workspaceId,
            @PathVariable Long objectionId,
            @Validated @RequestBody VoteRequest request
    ) {
        voteService.voteToObjection(user, workspaceId, objectionId, request.getWillApprove());
        return ResponseEntity.ok().build();
    }

}
