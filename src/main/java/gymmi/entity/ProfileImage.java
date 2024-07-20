package gymmi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage {

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
