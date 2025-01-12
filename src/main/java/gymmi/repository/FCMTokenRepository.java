package gymmi.repository;

import gymmi.entity.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

    @Query("select f from FCMToken f where f.user.id =:userId")
    Optional<FCMToken> findByUserId(Long userId);

}
