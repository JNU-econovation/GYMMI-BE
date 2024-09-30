package gymmi.repository;

import gymmi.entity.ProfileImage;
import gymmi.exception.NotFoundResourcesException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    @Query("select p from ProfileImage p where p.owner.id =:userId")
    Optional<ProfileImage> findByUserId(Long userId);

    default ProfileImage getByUserId(Long userId) {
        return findByUserId(userId)
                .orElseThrow(() -> new NotFoundResourcesException("프로필 이미지가 존재하지 않습니다."));
    }

}
