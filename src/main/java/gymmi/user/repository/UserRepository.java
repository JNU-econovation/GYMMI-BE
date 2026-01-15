package gymmi.user.repository;

import gymmi.user.domain.User;
import gymmi.global.exception.exceptiontype.NotFoundException;
import gymmi.global.exception.message.ErrorCode;
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

    default User findByIdOrThrow(Long userId) {
        return findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }
}
