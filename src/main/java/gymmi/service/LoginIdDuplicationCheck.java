package gymmi.service;

import gymmi.global.DuplicationCheckType;
import gymmi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginIdDuplicationCheck implements DuplicationCheck {

    private final UserRepository userRepository;

    @Override
    public boolean supports(DuplicationCheckType type) {
        return type == DuplicationCheckType.LOGIN_ID;
    }

    @Override
    public boolean isDuplicate(String value) {
        return userRepository.findByLoginId(value).isPresent();
    }
}
