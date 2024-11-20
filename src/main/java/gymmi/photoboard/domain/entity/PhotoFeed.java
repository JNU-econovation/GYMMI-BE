package gymmi.photoboard.domain.entity;

import gymmi.entity.TimeEntity;
import gymmi.entity.User;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import gymmi.exceptionhandler.message.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Getter
public class PhotoFeed extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private Integer thumpsUpCount;

    public PhotoFeed(User user, String comment) {
        this.user = user;
        this.comment = comment;
        this.thumpsUpCount = 0;
    }

    public boolean isModified() {
        return !getCreatedAt().isEqual(getLastModifiedAt());
    }

    public void increase() {
        thumpsUpCount = thumpsUpCount + 1;
    }

    public void decrease() {
        thumpsUpCount = thumpsUpCount - 1;
    }

    public void checkWriter(User user) {
        if (!this.user.equals(user)) {
            throw new NotHavePermissionException(ErrorCode.NOT_PHOTO_FEED_WRITER);
        }
    }
}
