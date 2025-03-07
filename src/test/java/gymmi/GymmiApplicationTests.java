package gymmi;

import gymmi.firebase.FirebaseTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(FirebaseTestConfig.class)
class GymmiApplicationTests {

    @Test
    void contextLoads() {
    }

}
