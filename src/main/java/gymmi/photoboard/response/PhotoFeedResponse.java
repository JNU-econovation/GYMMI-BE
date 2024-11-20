package gymmi.photoboard.response;

import gymmi.photoboard.domain.entity.PhotoFeed;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PhotoFeedResponse {

    private final String profileImageUrl;
    private final String photoImageUrl;
    private final String comment;
    private final Integer thumpsUpCount;
    private final LocalDateTime createdAt;
    private final Boolean isModified;

    public PhotoFeedResponse(PhotoFeed photoFeed, String photoImageUrl) {
        this.profileImageUrl = photoFeed.getUser().getProfileImageName();
        this.photoImageUrl = photoImageUrl;
        this.comment = photoFeed.getComment();
        this.thumpsUpCount = photoFeed.getThumpsUpCount();
        this.createdAt = photoFeed.getCreatedAt();
        this.isModified = photoFeed.isModified();
    }

}

