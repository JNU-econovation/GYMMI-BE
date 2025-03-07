package gymmi;

import jakarta.persistence.EntityListeners;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GymmiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymmiApplication.class, args);
    }

}
