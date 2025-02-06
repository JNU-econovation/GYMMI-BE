package gymmi.service;

import gymmi.entity.FcmToken;
import gymmi.entity.User;
import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.repository.FcmTokenRepository;
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

