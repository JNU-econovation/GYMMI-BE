package gymmi.repository;

import gymmi.entity.Logined;
import gymmi.exception.NotFoundResourcesException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LoginedRepository extends JpaRepository<Logined, Long> {

    default Logined getByUserId(Long userId) {
        return findByUserId(userId)
                .orElseThrow(() -> new NotFoundResourcesException("정보를 찾을 수 없습니다."));
    }

    @Query("select lo from Logined lo where lo.user.id =:userId")
    Optional<Logined> findByUserId(Long userId);


}
