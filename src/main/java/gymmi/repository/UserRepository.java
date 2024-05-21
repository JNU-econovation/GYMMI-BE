package gymmi.repository;

import gymmi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.loginId = :loginId")
    Optional<User> findByLoginId(String loginId);

}
