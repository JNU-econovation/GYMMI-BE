package gymmi.service;

import gymmi.entity.Logined;
import gymmi.entity.User;
import gymmi.exception.AlreadyExistException;
import gymmi.exception.AuthenticationException;
import gymmi.exception.NotMatchedException;
import gymmi.repository.LoginedRepository;
import gymmi.repository.UserRepository;
import gymmi.request.LoginRequest;
import gymmi.request.RegistrationRequest;
import gymmi.request.ReissueRequest;
import gymmi.response.LoginResponse;
import gymmi.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProcessor tokenProcessor;
    private final UserRepository userRepository;
    private final LoginedRepository loginedRepository;

    @Transactional
    public void registerUser(RegistrationRequest request) {
        if (userRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new AlreadyExistException("이미 등록된 아이디 입니다.");
        }
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new AlreadyExistException("이미 존재하는 닉네임 입니다.");
        }
        User newUser = User.builder()
                .loginId(request.getLoginId())
                .plainPassword(request.getPassword())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .build();
        User savedUser = userRepository.save(newUser);
        loginedRepository.save(new Logined(savedUser));
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new NotMatchedException("아이디와 비밀번호를 확인해 주세요."));

        if (!user.canAuthenticate(request.getLoginId(), request.getPassword())) {
            throw new NotMatchedException("아이디와 비밀번호를 확인해 주세요.");
        }

        TokenResponse tokenResponse = generateAndSaveTokensAbout(user);
        return LoginResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileURL("")
                .refreshToken(tokenResponse.getRefreshToken())
                .accessToken(tokenResponse.getAccessToken())
                .build();
    }

    @Transactional
    public TokenResponse reissue(ReissueRequest request) {
        Long userId = tokenProcessor.parseRefreshToken(request.getRefreshToken());
        User user = userRepository.getByUserId(userId);
        Logined logined = loginedRepository.getByUserId(userId);

        if (!logined.isActivatedRefreshToken(request.getRefreshToken())) {
            logined.destroyRefreshToken();
            throw new AuthenticationException("잘못된 접근입니다. 다시 로그인 해주세요.");
        }

        return generateAndSaveTokensAbout(user);
    }

    private TokenResponse generateAndSaveTokensAbout(User user) {
        String accessToken = tokenProcessor.generateAccessToken(user.getId());
        String refreshToken = tokenProcessor.generateRefreshToken(user.getId());
        Logined logined = loginedRepository.getByUserId(user.getId());
        logined.saveRefreshToken(refreshToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public void logout(User loginedUser) {
        Logined logined = loginedRepository.getByUserId(loginedUser.getId());
        logined.destroyRefreshToken();
    }

    @Transactional
    public void resign(User loginedUser) {
        loginedUser.resign();
        // 프로필사진 지우기
    }
}
