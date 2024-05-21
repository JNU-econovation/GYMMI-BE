package gymmi.service;

import gymmi.entity.User;
import gymmi.exception.AlreadyExistException;
import gymmi.repository.UserRepository;
import gymmi.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

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
        userRepository.save(newUser);
    }
}
