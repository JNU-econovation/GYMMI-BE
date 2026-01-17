package gymmi.fcmtoken.service;

import gymmi.fcmtoken.domain.FcmToken;
import gymmi.user.domain.User;
import gymmi.global.exception.exceptiontype.NotFoundException;
import gymmi.global.exception.message.ErrorCode;
import gymmi.fcmtoken.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void refresh(User loginedUser, String token) {
        FcmToken fcmToken = fcmTokenRepository.findByUserId(loginedUser.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.RETRY_AFTER_LOGOUT));
        fcmToken.set(token);
    }

    @Transactional
    public void delete(User loginedUser) {
        FcmToken fcmToken = fcmTokenRepository.findByUserId(loginedUser.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.RETRY_AFTER_LOGOUT));
        fcmToken.delete();
    }
}

