package gymmi.photoboard.service;

import gymmi.entity.User;
import gymmi.photoboard.domain.entity.PhotoFeed;
import gymmi.photoboard.domain.entity.PhotoFeedImage;
import gymmi.photoboard.repository.PhotoFeedImageRepository;
import gymmi.photoboard.repository.PhotoFeedRepository;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
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
}
