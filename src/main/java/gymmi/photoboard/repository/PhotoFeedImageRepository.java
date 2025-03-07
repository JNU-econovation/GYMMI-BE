package gymmi.photoboard.repository;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.photoboard.domain.entity.PhotoFeedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PhotoFeedImageRepository extends JpaRepository<PhotoFeedImage, Long> {


    @Query("select p from PhotoFeedImage p where p.photoFeed.id =:photoFeedId")
    Optional<PhotoFeedImage> findByPhotoFeedId(Long photoFeedId);

    default PhotoFeedImage getByPhotoFeedId(Long photoFeedId) {
        return findByPhotoFeedId(photoFeedId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PHOTO_FEED_IMAGE));
    }

}
