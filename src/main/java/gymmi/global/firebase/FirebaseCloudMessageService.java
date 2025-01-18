package gymmi.global.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseCloudMessageService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendMessage(SendingRequest request) {
        if (request.getUserToken() == null) {
            log.info("토큰값 null이라서 전송 안됨.");
            return;
        }
        log.info("토큰값: {}, 알림 메시지내용: {}", request.getUserToken(), request.getBody());
        Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(request.getUserToken())
                .putData("redirectUrl", request.getRedirectUrl())
                .putData("createdAt", request.getCreatedAt())
                .build();
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            log.info("메시지 보내기 에러로 인해 전송 실패");
        }
        log.info("알림 메시지 보내기 성공.");
    }
}
