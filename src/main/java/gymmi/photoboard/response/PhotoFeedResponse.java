package gymmi.photoboard.response;

import gymmi.photoboard.domain.entity.PhotoFeed;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PhotoFeedResponse {

    private final Long photoId;
    private final String photoImageUrl;
    private final LocalDateTime createdAt;

    public PhotoFeedResponse(Long photoId, String photoImageUrl, LocalDateTime createdAt) {
        this.photoId = photoId;
        this.photoImageUrl = photoImageUrl;
        this.createdAt = createdAt;
    }
}

