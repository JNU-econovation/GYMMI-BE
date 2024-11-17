package gymmi.photoboard.repository;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.photoboard.domain.entity.PhotoFeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoFeedRepository extends JpaRepository<PhotoFeed, Long> {

    default PhotoFeed getByPhotoFeedId(Long photoFeedId) {
        return findById(photoFeedId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PHOTO_FEED));
    }

}
