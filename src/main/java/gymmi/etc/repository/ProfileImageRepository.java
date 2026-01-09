package gymmi.etc.repository;

import gymmi.etc.domain.entity.ProfileImage;
import gymmi.global.exceptionhandler.exception.NotFoundException;
import gymmi.global.exceptionhandler.message.ErrorCode;
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
