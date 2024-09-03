package gymmi.repository;

import gymmi.entity.User;
import gymmi.exception.NotFoundResourcesException;
import gymmi.repository.custom.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

    @Query("select u from User u where u.loginId = :loginId and u.isResigned = false")
    Optional<User> findByLoginId(String loginId);

    @Query("select u from User u where u.id = :userId")
    Optional<User> findByUserId(Long userId);

    @Query("select u from User u where u.nickname = :nickname")
    Optional<User> findByNickname(String nickname);

    default User getByUserId(Long userId) {
        return findByUserId(userId)
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 사용자입니다."));
    }
}
