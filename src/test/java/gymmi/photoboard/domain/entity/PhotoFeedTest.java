package gymmi.photoboard.domain.entity;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PhotoFeedTest {

    @ParameterizedTest
    @CsvSource(value = {"1,true", "0,false"})
    void 수정여부를_확인_한다(int plusDay, boolean isModified) {
        // given
        LocalDateTime now = LocalDateTime.now();
        PhotoFeed photoFeed = Instancio.of(PhotoFeed.class)
                .set(Select.field(PhotoFeed::getCreatedAt), now)
                .set(Select.field(PhotoFeed::getLastModifiedAt), now.plusDays(plusDay))
                .create();

        // when
        boolean result = photoFeed.isModified();

        // then
        assertThat(result).isEqualTo(isModified);
    }

}
