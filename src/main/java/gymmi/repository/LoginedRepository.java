package gymmi.repository;

import gymmi.entity.Logined;
import gymmi.exceptionhandler.legacy.NotFoundResourcesException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoginedRepository extends JpaRepository<Logined, Long> {

    default Logined getByUserId(Long userId) {
        return findByUserId(userId)
                .orElseThrow(() -> new NotFoundResourcesException("정보를 찾을 수 없습니다."));
    }

    @Query("select lo from Logined lo where lo.user.id =:userId")
    Optional<Logined> findByUserId(Long userId);


}
