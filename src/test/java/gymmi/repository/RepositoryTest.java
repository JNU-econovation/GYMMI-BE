package gymmi.repository;

import gymmi.global.QuerydslConfig;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(QuerydslConfig.class)
@DataJpaTest
public class RepositoryTest {

    @Autowired
    EntityManager entityManager;

}
