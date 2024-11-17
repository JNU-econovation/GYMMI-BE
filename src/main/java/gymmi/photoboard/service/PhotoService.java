package gymmi.photoboard.service;

import gymmi.entity.User;
import gymmi.photoboard.domain.entity.PhotoFeed;
import gymmi.photoboard.domain.entity.PhotoFeedImage;
import gymmi.photoboard.repository.PhotoFeedImageRepository;
import gymmi.photoboard.repository.PhotoFeedRepository;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.photoboard.response.PhotoFeedResponse;
import gymmi.service.ImageUse;
import gymmi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final S3Service s3Service;
    private final PhotoFeedRepository photoFeedRepository;
    private final PhotoFeedImageRepository photoFeedImageRepository;

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

        String photoImagePresignedUrl = s3Service.getPresignedUrl(ImageUse.PHOTO_FEED, photoFeedImage.getFilename());
        return new PhotoFeedResponse(photoFeed, photoImagePresignedUrl);
    }

}
