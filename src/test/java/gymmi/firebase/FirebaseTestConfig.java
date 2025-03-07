package gymmi.firebase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FirebaseTestConfig {
    @Bean
    public FirebaseApp firebaseApp() {
        return Mockito.mock(FirebaseApp.class);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        return Mockito.mock(FirebaseMessaging.class);
    }
}
