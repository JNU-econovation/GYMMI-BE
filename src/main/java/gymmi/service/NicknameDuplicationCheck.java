package gymmi.service;

import gymmi.entity.User;
import gymmi.global.DuplicationCheckType;
import gymmi.repository.UserRepository;
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
