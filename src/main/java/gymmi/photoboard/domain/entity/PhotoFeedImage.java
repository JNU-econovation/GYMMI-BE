package gymmi.photoboard.domain.entity;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photo_feed_id", nullable = false)
    private PhotoFeed photoFeed;

    @Column(nullable = false)
    private String filename;

    public PhotoFeedImage(PhotoFeed photoFeed, String filename) {
        this.photoFeed = photoFeed;
        this.filename = filename;
    }
}
