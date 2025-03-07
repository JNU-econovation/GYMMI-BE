package gymmi.workspace.service;


import gymmi.firebase.FirebaseTestConfig;
import gymmi.helper.Persister;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Transactional
@Import({Persister.class, FirebaseTestConfig.class})
public class IntegrationTest {

    @Autowired
    public Persister persister;

}
