package gymmi.repository;

import gymmi.entity.Logined;
import gymmi.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    @Query("select p from ProfileImage p where p.owner.id =:userId")
    Optional<ProfileImage> findByUserId(Long userId);

}
