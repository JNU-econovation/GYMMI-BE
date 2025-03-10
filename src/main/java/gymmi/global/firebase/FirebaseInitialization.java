package gymmi.global.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@Profile("!test")
public class FirebaseInitialization {


    @Value("${firebase.key-path}")
    private String path;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        deleteIfExist();

        FileInputStream serviceAccount = new FileInputStream(path);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }

    private void deleteIfExist() {
        try {
            FirebaseApp existingApp = FirebaseApp.getInstance();
            existingApp.delete();
        } catch (IllegalStateException ignored) {

        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

}
