package gymmi.global.common.eventlistener.event;

import lombok.Getter;

@Getter
public class LinkToPhotoFeedEvent {

    private final Long userId;
    private final String imageUrl;
    private final String comment;

    public LinkToPhotoFeedEvent(Long userId, String imageUrl, String comment) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.comment = comment;
    }
}
