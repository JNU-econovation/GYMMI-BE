package gymmi.controller;

import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.request.FCMRefreshRequest;
import gymmi.service.FcmTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FcmTokenService fcmTokenService;

    @PatchMapping("/fcm/token")
    public ResponseEntity<Void> refresh(
            @Logined User user,
            @Validated @RequestBody FCMRefreshRequest request
    ) {
        fcmTokenService.refresh(user, request.getToken());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/fcm/token")
    public ResponseEntity<Void> delete(
            @Logined User user
    ) {
        fcmTokenService.delete(user);
        return ResponseEntity.ok().build();
    }

}
