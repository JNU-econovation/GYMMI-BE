package gymmi.repository;

import gymmi.entity.Logined;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoginedRepository extends JpaRepository<Logined, Long> {

    //    @Query("select lo from Logined lo join User u on lo.user.id = u.id where u.id = :userId")
    @Query("select lo from Logined lo join lo.user where lo.user.id = :userId")
    Logined getByUserId(Long userId);
}
