package gymmi.service;

import gymmi.entity.Logined;
import gymmi.entity.ProfileImage;
import gymmi.entity.User;
import gymmi.exception.class1.AuthenticationFailException;
import gymmi.exception.class1.AlreadyExistException;
import gymmi.exception.class1.NotMatchedException;
import gymmi.exception.message.ErrorCode;
import gymmi.repository.LoginedRepository;
import gymmi.repository.ProfileImageRepository;
import gymmi.repository.UserRepository;
import gymmi.request.LoginRequest;
import gymmi.request.RegistrationRequest;
import gymmi.request.ReissueRequest;
import gymmi.request.ResignRequest;
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
    private final ProfileImageRepository profileImageRepository;
    private final ImageFileUploader imageFileUploader;

    @Transactional
    public void registerUser(RegistrationRequest request) {
        if (userRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new AlreadyExistException(ErrorCode.ALREADY_USED_LOGIN_ID);
        }
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new AlreadyExistException(ErrorCode.ALREADY_USED_NICKNAME);
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
                .orElseThrow(() -> new AuthenticationFailException(ErrorCode.FAILED_LOGIN));

        if (!user.canAuthenticate(request.getLoginId(), request.getPassword())) {
            throw new AuthenticationFailException(ErrorCode.FAILED_LOGIN);
        }

        TokenResponse tokenResponse = generateAndSaveTokensAbout(user);
        return LoginResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileURL(user.getProfileImageName())
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
            throw new AuthenticationFailException(ErrorCode.UNUSUAL_AUTHORIZATION_ACCESS);
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
    public void resign(User loginedUser, ResignRequest request) {
        if (!loginedUser.canAuthenticate(request.getPassword())) {
            throw new NotMatchedException(ErrorCode.NOT_MATCHED_PASSWORD);
        }
        loginedUser.resign();
        ProfileImage profileImage = profileImageRepository.getByUserId(loginedUser.getId());
        profileImageRepository.delete(profileImage);
        imageFileUploader.delete(profileImage.getStoredName());
    }
}
