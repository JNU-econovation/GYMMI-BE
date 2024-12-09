package gymmi.photoboard.service;

import gymmi.entity.User;
import gymmi.photoboard.domain.entity.PhotoFeed;
import gymmi.photoboard.domain.entity.PhotoFeedImage;
import gymmi.photoboard.repository.PhotoFeedImageRepository;
import gymmi.photoboard.repository.PhotoFeedRepository;
import gymmi.photoboard.repository.ThumbsUpRepository;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.photoboard.response.PhotoFeedDetailResponse;
import gymmi.photoboard.response.PhotoFeedResponse;
import gymmi.service.S3Service;
import gymmi.workspace.service.IntegrationTest;
import jakarta.persistence.EntityManager;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PhotoFeedServiceTest extends IntegrationTest {

    @Autowired
    PhotoFeedService photoFeedService;

    @Autowired
    PhotoFeedRepository photoFeedRepository;

    @Autowired
    PhotoFeedImageRepository photoFeedImageRepository;

    @Autowired
    ThumbsUpRepository thumbsUpRepository;

    @Autowired
    EntityManager entityManager;

    @MockBean
    S3Service s3Service;

    @Test
    void 사진_등록을_한다() {
        // given
        User user = persister.persistUser();
        CreatePhotoFeedRequest request = new CreatePhotoFeedRequest(UUID.randomUUID().toString(), Instancio.gen().string().get());

        // when
        photoFeedService.createPhotoFeed(user, request);

        // then
        assertThat(photoFeedRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 사진_피드를_확인_한다() {
        // given
        User user = persister.persistUser();
        PhotoFeed photoFeed = persister.persistPhotoFeed(user, false);
        PhotoFeedImage photoFeedImage = persister.persistPhotoFeedImage(photoFeed);

        // when
        PhotoFeedDetailResponse result = photoFeedService.getPhotoFeed(user, photoFeed.getId());

        // then
        assertThat(result.getIsModified()).isFalse();
        assertThat(result.getThumpsUpCount()).isEqualTo(photoFeed.getThumpsUpCount());
        assertThat(result.getCreatedAt()).isEqualTo(photoFeed.getCreatedAt());
        assertThat(result.getProfileImageUrl()).isEqualTo(user.getProfileImageName());
        assertThat(result.getComment()).isEqualTo(photoFeed.getComment());
        assertThat(result.getNickname()).isEqualTo(user.getNickname());
        assertThat(result.getIsMine()).isEqualTo(true);
        assertThat(result.getHasMyThumbsUp()).isEqualTo(false);
    }

    @Test
    void 사진_피드에_좋아요를_누르거나_취소한다() {
        // given
        User user = persister.persistUser();
        User user1 = persister.persistUser();
        PhotoFeed photoFeed = persister.persistPhotoFeed(user, 0);
        PhotoFeedImage photoFeedImage = persister.persistPhotoFeedImage(photoFeed);

        // when
        photoFeedService.likePhotoFeed(user1, photoFeed.getId());

        // then
        assertThat(photoFeed.getThumpsUpCount()).isEqualTo(1);
        assertThat(thumbsUpRepository.findByUserIdAndPhotoFeedId(user1.getId(), photoFeed.getId())).isNotEmpty();

        // when
        photoFeedService.likePhotoFeed(user1, photoFeed.getId());

        // then
        assertThat(photoFeed.getThumpsUpCount()).isEqualTo(0);
        assertThat(thumbsUpRepository.findByUserIdAndPhotoFeedId(user1.getId(), photoFeed.getId())).isEmpty();
    }

    @Test
    void 사진_피드를_삭제한다() {
        // given
        User user = persister.persistUser();
        PhotoFeed photoFeed = persister.persistPhotoFeed(user);
        PhotoFeedImage photoFeedImage = persister.persistPhotoFeedImage(photoFeed);

        // when
        photoFeedService.delete(user, photoFeed.getId());

        // then
        entityManager.flush();
        entityManager.clear();
        assertThat(photoFeedImageRepository.findByPhotoFeedId(photoFeed.getId())).isEmpty();
        assertThat(photoFeedRepository.findById(photoFeed.getId())).isEmpty();
    }

    @Test
    void 사진_피드_목록을_확인한다() {
        // given
        User user = persister.persistUser();
        PhotoFeed photoFeed = persister.persistPhotoFeed(user);
        PhotoFeedImage photoFeedImage = persister.persistPhotoFeedImage(photoFeed);
        PhotoFeed photoFeed1 = persister.persistPhotoFeed(user);
        PhotoFeedImage photoFeedImage1 = persister.persistPhotoFeedImage(photoFeed1);

        // when
        List<PhotoFeedResponse> responses = photoFeedService.getPhotoFeeds(0);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getPhotoId()).isEqualTo(photoFeedImage1.getId());
        assertThat(responses.get(1).getPhotoId()).isEqualTo(photoFeedImage.getId());
    }
}
