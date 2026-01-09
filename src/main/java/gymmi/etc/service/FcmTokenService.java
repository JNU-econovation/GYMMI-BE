package gymmi.etc.service;

import gymmi.etc.domain.entity.FcmToken;
import gymmi.etc.domain.entity.User;
import gymmi.global.exceptionhandler.exception.NotFoundException;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.etc.repository.FcmTokenRepository;
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

