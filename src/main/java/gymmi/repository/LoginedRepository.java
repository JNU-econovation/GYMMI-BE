package gymmi.repository;

import gymmi.entity.Logined;
import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LoginedRepository extends JpaRepository<Logined, Long> {

    default Logined getByUserId(Long userId) {
        return findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

    @Query("select lo from Logined lo where lo.user.id =:userId")
    Optional<Logined> findByUserId(Long userId);


}
