package gymmi.service;

import gymmi.entity.FCMToken;
import gymmi.entity.User;
import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.repository.FCMTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FCMTokenService {

    private final FCMTokenRepository fcmTokenRepository;

    @Transactional
    public void refresh(User loginedUser, String token) {
        FCMToken fcmToken = fcmTokenRepository.findByUserId(loginedUser.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.RETRY_AFTER_LOGOUT));
        fcmToken.set(token);
    }

    @Transactional
    public void delete(User loginedUser) {
        FCMToken fcmToken = fcmTokenRepository.findByUserId(loginedUser.getId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.RETRY_AFTER_LOGOUT));
        fcmToken.delete();
    }
}

