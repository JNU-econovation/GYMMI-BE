package gymmi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Lob
    @Column(nullable = false)
    private String content;

    public Feedback(User user, String content) {
        this.user = user;
        this.content = content;
    }
}
