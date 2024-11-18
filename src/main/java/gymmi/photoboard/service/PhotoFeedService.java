package gymmi.photoboard.service;

import gymmi.entity.User;
import gymmi.photoboard.domain.entity.PhotoFeed;
import gymmi.photoboard.domain.entity.PhotoFeedImage;
import gymmi.photoboard.domain.entity.ThumbsUp;
import gymmi.photoboard.repository.PhotoFeedImageRepository;
import gymmi.photoboard.repository.PhotoFeedRepository;
import gymmi.photoboard.repository.ThumbsUpRepository;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.photoboard.response.PhotoFeedResponse;
import gymmi.service.ImageUse;
import gymmi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhotoFeedService {
    private static final ImageUse PHOTO_FEED = ImageUse.PHOTO_FEED;

    private final S3Service s3Service;
    private final PhotoFeedRepository photoFeedRepository;
    private final PhotoFeedImageRepository photoFeedImageRepository;
    private final ThumbsUpRepository thumbsUpRepository;

    @Transactional
    public void createPhotoFeed(User loginedUser, CreatePhotoFeedRequest request) {
        PhotoFeed photoFeed = new PhotoFeed(loginedUser, request.getComment());
        PhotoFeedImage photoFeedImage = new PhotoFeedImage(photoFeed, request.getFilename());
        photoFeedRepository.save(photoFeed);
        photoFeedImageRepository.save(photoFeedImage);
    }

    @Transactional(readOnly = true)
    public PhotoFeedResponse getPhotoFeed(Long photoFeedId) {
        PhotoFeed photoFeed = photoFeedRepository.getByPhotoFeedId(photoFeedId);
        PhotoFeedImage photoFeedImage = photoFeedImageRepository.getByPhotoFeedId(photoFeedId);

        String photoImagePresignedUrl = s3Service.getPresignedUrl(PHOTO_FEED, photoFeedImage.getFilename());
        return new PhotoFeedResponse(photoFeed, photoImagePresignedUrl);
    }

    @Transactional
    public void likePhotoFeed(User loginedUser, Long photoFeedId) {
        PhotoFeed photoFeed = photoFeedRepository.getByPhotoFeedId(photoFeedId);
        thumbsUpRepository.findByUserId(loginedUser.getId())
                .ifPresentOrElse(
                        (thumbsUp) -> {
                            photoFeed.decrease();
                            thumbsUpRepository.delete(thumbsUp);
                        },
                        () -> {
                            ThumbsUp thumbsUp = new ThumbsUp(loginedUser, photoFeed);
                            photoFeed.increase();
                            thumbsUpRepository.save(thumbsUp);
                        }
                );
    }

    @Transactional
    public void delete(User loginedUser, Long photoFeedId) {
        PhotoFeed photoFeed = photoFeedRepository.getByPhotoFeedId(photoFeedId);

        photoFeed.checkWriter(loginedUser);

        PhotoFeedImage photoFeedImage = photoFeedImageRepository.getByPhotoFeedId(photoFeedId);
        photoFeedImageRepository.delete(photoFeedImage);
        photoFeedRepository.delete(photoFeed);
        s3Service.delete(PHOTO_FEED, photoFeedImage.getFilename());
    }
}
