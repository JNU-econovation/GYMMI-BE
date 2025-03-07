package gymmi.repository;

import gymmi.entity.ProfileImage;
import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    @Query("select p from ProfileImage p where p.owner.id =:userId")
    Optional<ProfileImage> findByUserId(Long userId);

    default ProfileImage getByUserId(Long userId) {
        return findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PHOTO_FEED_IMAGE));
    }

}
