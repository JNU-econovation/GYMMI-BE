package gymmi.photoboard.service;

import gymmi.entity.User;
import gymmi.photoboard.domain.entity.PhotoFeed;
import gymmi.photoboard.domain.entity.PhotoFeedImage;
import gymmi.photoboard.repository.PhotoFeedRepository;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.photoboard.response.PhotoFeedResponse;
import gymmi.service.S3Service;
import gymmi.workspace.service.IntegrationTest;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PhotoServiceTest extends IntegrationTest {

    @Autowired
    PhotoService photoService;

    @Autowired
    PhotoFeedRepository photoFeedRepository;

    @MockBean
    S3Service s3Service;

    @Test
    void 사진_등록을_한다() {
        // given
        User user = persister.persistUser();
        CreatePhotoFeedRequest request = new CreatePhotoFeedRequest(UUID.randomUUID().toString(), Instancio.gen().string().get());

        // when
        photoService.createPhotoFeed(user, request);

        // then
        assertThat(photoFeedRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 사진_피드를_확인_한다() {
        // given
        User user = persister.persistUser();
        PhotoFeed photoFeed = persister.persistPhotoFeed(user, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        PhotoFeedImage photoFeedImage = persister.persistPhotoFeedImage(photoFeed);

        // when
        PhotoFeedResponse result = photoService.getPhotoFeed(photoFeed.getId());

        // then
        assertThat(result.getIsModified()).isFalse();
        assertThat(result.getThumpsUpCount()).isEqualTo(photoFeed.getThumpsUpCount());
        assertThat(result.getCreatedAt()).isEqualTo(photoFeed.getCreatedAt());
        assertThat(result.getProfileImageUrl()).isEqualTo(user.getProfileImageName());
        assertThat(result.getComment()).isEqualTo(photoFeed.getComment());
    }
}
