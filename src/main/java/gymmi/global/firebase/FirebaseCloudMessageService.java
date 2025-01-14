package gymmi.global.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendMessage(SendingRequest request) {
        if (request.getUserToken() == null) {
            return;
        }
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
        }
    }
}
