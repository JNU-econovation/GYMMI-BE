package gymmi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage extends TimeEntity {

    public static final String EMPTY_NAME = "default.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false, unique = true, updatable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private User owner;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String storedName;

    @Builder
    public ProfileImage(User owner, String originName, String storedName) {
        this.owner = owner;
        this.originName = originName;
        this.storedName = storedName;
    }

    public Long getId() {
        return id;
    }

    public String getOriginName() {
        return originName;
    }

    public String getStoredName() {
        return storedName;
    }
}
