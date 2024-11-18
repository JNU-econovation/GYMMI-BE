package gymmi.photoboard.domain.entity;

import gymmi.entity.User;
import gymmi.exceptionhandler.message.ErrorCode;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void 작성자가_아닌_경우_예외가_발생한다() {
        // given
        User user = Instancio.of(User.class).create();
        PhotoFeed photoFeed = Instancio.of(PhotoFeed.class)
                .filter(Select.field(PhotoFeed::getUser), (User u) -> !user.equals(u))
                .create();

        // when, then
        assertThatThrownBy(() -> photoFeed.checkWriter(user))
                .hasMessage(ErrorCode.NOT_PHOTO_FEED_WRITER.getMessage());
    }

}
