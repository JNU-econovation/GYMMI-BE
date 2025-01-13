package gymmi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class FcmToken extends TimeEntity {

    private static final LocalDateTime INITIAL_TIME = LocalDateTime.of(1970, 1, 2, 0, 0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false, unique = true, updatable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private String token;

    @Column(columnDefinition = "timestamp(3)", nullable = false)
    private LocalDateTime lastUsedTime;

    public FcmToken(User user) {
        this.user = user;
        setDefaultTimestamp();
    }

    public void set(String token) {
        this.token = token;
        refreshTimestamp();
    }

    public void delete() {
        this.token = null;
        setDefaultTimestamp();
    }

    private void refreshTimestamp() {
        this.lastUsedTime = LocalDateTime.now();
    }

    private void setDefaultTimestamp() {
        this.lastUsedTime = INITIAL_TIME;
    }
}
