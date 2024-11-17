package gymmi.photoboard.service;

import gymmi.entity.User;
import gymmi.photoboard.repository.PhotoFeedRepository;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.workspace.service.IntegrationTest;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PhotoServiceTest extends IntegrationTest {

    @Autowired
    PhotoService photoService;

    @Autowired
    PhotoFeedRepository photoFeedRepository;

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
}
