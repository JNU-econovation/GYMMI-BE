package gymmi.workspace.service;


import gymmi.helper.Persister;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Transactional
@Import(Persister.class)
public class IntegrationTest {

    @Autowired
    Persister persister;

}
