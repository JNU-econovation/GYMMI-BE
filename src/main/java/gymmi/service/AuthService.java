package gymmi.service;

import gymmi.entity.Logined;
import gymmi.entity.User;
import gymmi.exception.AlreadyExistException;
import gymmi.exception.NotMatchedException;
import gymmi.repository.LoginedRepository;
import gymmi.repository.UserRepository;
import gymmi.request.LoginRequest;
import gymmi.request.RegistrationRequest;
import gymmi.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProcessor tokenProcessor;
    private final UserRepository userRepository;
    private final LoginedRepository loginedRepository;

    public void registerUser(RegistrationRequest request) {
        if (userRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new AlreadyExistException("이미 등록된 아이디 입니다.");
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

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new NotMatchedException("아이디와 비밀번호를 확인해 주세요"));
        user.authenticate(request.getLoginId(), request.getPassword());

        String accessToken = tokenProcessor.generateAccessToken(user.getId());
        String refreshToken = tokenProcessor.generateRefreshToken(user.getId());
        Logined logined = loginedRepository.getByUserId(user.getId());
        logined.saveRefreshToken(refreshToken);
        return new LoginResponse(accessToken, refreshToken);
    }
}
