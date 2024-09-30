package gymmi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Logined extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false, unique = true, updatable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private String refreshToken;

    public Logined(User user) {
        this.user = user;
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    public boolean isActivatedRefreshToken(String refreshToken) {
        if (this.refreshToken == null) {
            return false;
        }
        return this.refreshToken.equals(refreshToken);
    }

}
