package gymmi.user.domain;

import gymmi.global.check.domain.DuplicationCheck;
import gymmi.global.check.domain.DuplicationCheckType;
import gymmi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NicknameDuplicationCheck implements DuplicationCheck {

    private final UserRepository userRepository;

    @Override
    public boolean supports(DuplicationCheckType type) {
        return type == DuplicationCheckType.NICKNAME;
    }

    @Override
    public boolean isDuplicate(String value) {
        User.validateNickname(value);
        return userRepository.findByNickname(value).isPresent();
    }
}
