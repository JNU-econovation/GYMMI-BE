package gymmi.photoboard.domain.entity;

import gymmi.global.common.entity.TimeEntity;
import gymmi.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "photo_feed_id"})})
public class ThumbsUp extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "photo_feed_id", nullable = false)
    private PhotoFeed photoFeed;

    public ThumbsUp(User user, PhotoFeed photoFeed) {
        this.user = user;
        this.photoFeed = photoFeed;
    }
}
