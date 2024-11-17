package gymmi.photoboard.domain.entity;

import gymmi.entity.TimeEntity;
import gymmi.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
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
}
