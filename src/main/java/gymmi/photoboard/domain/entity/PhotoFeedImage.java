package gymmi.photoboard.domain.entity;

import gymmi.service.ImageUse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@Getter
public class PhotoFeedImage {

    public static final ImageUse IMAGE_USE = ImageUse.PHOTO_FEED;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photo_feed_id", nullable = false, unique = true)
    private PhotoFeed photoFeed;

    @Column(nullable = false)
    private String filename;

    public PhotoFeedImage(PhotoFeed photoFeed, String filename) {
        this.photoFeed = photoFeed;
        this.filename = filename;
    }
}
