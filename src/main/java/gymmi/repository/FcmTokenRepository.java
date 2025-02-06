package gymmi.repository;

import gymmi.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    @Query("select f from FcmToken f where f.user.id =:userId")
    Optional<FcmToken> findByUserId(Long userId);

}
