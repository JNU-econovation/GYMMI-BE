package gymmi.controller;

import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.request.LoginRequest;
import gymmi.request.RegistrationRequest;
import gymmi.request.ReissueRequest;
import gymmi.response.TokensResponse;
import gymmi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/join")
    public ResponseEntity<Void> registerUser(@Validated @RequestBody RegistrationRequest request) {
        authService.registerUser(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/welcome")
    public ResponseEntity<TokensResponse> login(@Validated @RequestBody LoginRequest request) {
        TokensResponse response = authService.login(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<TokensResponse> reissue(@Validated @RequestBody ReissueRequest request) {
        TokensResponse response = authService.reissue(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/auth/goodbye")
    public ResponseEntity<Void> logout(@Logined User user) {
        authService.logout(user);
        return ResponseEntity.ok().build();
    }

}
