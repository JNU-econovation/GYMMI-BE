package gymmi.photoboard.service;

import gymmi.entity.User;
import gymmi.photoboard.domain.entity.PhotoFeed;
import gymmi.photoboard.domain.entity.PhotoFeedImage;
import gymmi.photoboard.domain.entity.ThumbsUp;
import gymmi.photoboard.repository.PhotoFeedImageRepository;
import gymmi.photoboard.repository.PhotoFeedRepository;
import gymmi.photoboard.repository.ThumbsUpRepository;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.photoboard.response.PhotoFeedDetailResponse;
import gymmi.photoboard.response.PhotoFeedResponse;
import gymmi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoFeedService {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private final S3Service s3Service;
    private final PhotoFeedRepository photoFeedRepository;
    private final PhotoFeedImageRepository photoFeedImageRepository;
    private final ThumbsUpRepository thumbsUpRepository;

    @Transactional
    public Long createPhotoFeed(User loginedUser, CreatePhotoFeedRequest request) {
        PhotoFeed photoFeed = new PhotoFeed(loginedUser, request.getComment());
        PhotoFeedImage photoFeedImage = new PhotoFeedImage(photoFeed, request.getFilename());
        s3Service.checkObjectExist(PhotoFeedImage.IMAGE_USE, photoFeedImage.getFilename());
        PhotoFeed savedPhotoFeed = photoFeedRepository.save(photoFeed);
        photoFeedImageRepository.save(photoFeedImage);
        return savedPhotoFeed.getId();
    }

    @Transactional(readOnly = true)
    public PhotoFeedDetailResponse getPhotoFeed(Long photoFeedId) {
        PhotoFeed photoFeed = photoFeedRepository.getByPhotoFeedId(photoFeedId);
        PhotoFeedImage photoFeedImage = photoFeedImageRepository.getByPhotoFeedId(photoFeedId);

        String photoImagePresignedUrl = s3Service.getPresignedUrl(PhotoFeedImage.IMAGE_USE, photoFeedImage.getFilename());
        return new PhotoFeedDetailResponse(photoFeed, photoImagePresignedUrl);
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
        s3Service.delete(PhotoFeedImage.IMAGE_USE, photoFeedImage.getFilename());
    }

    public List<PhotoFeedResponse> getPhotoFeeds(int pageNumber) {
        Page<PhotoFeed> photoFeeds = photoFeedRepository.findAll(PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, Sort.by("createdAt").descending()));
        List<PhotoFeedResponse> responses = new ArrayList<>();
        for (PhotoFeed photoFeed : photoFeeds) {
            PhotoFeedImage photoFeedImage = photoFeedImageRepository.getByPhotoFeedId(photoFeed.getId());
            String photoImagePresignedUrl = s3Service.getPresignedUrl(PhotoFeedImage.IMAGE_USE, photoFeedImage.getFilename());
            responses.add(new PhotoFeedResponse(photoFeed.getId(), photoImagePresignedUrl, photoFeed.getCreatedAt()));
        }
        return responses;
    }
}
