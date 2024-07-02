package gymmi.entity;


import gymmi.exception.NotFoundResourcesException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "workspace_id"})})
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @JoinColumn(name = "workspace_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Workspace workspace;

    @Column(nullable = false)
    private Integer contributedScore;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Worker(User user, Workspace workspace) {
        this.user = user;
        this.workspace = workspace;
        this.contributedScore = 0;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getNickname() {
        return user.getNickname();
    }

    public Integer getContributedScore() {
        return contributedScore;
    }

    public void addWorkingScore(Integer workingScore) {
        contributedScore += workingScore;
    }

    public Working doMission(Mission mission, Integer count) {
        if (!this.workspace.isRegisteredMission(mission)) {
            throw new NotFoundResourcesException("해당 미션이 존재하지 않아요.");
        }
        return Working.builder()
                .mission(mission)
                .worker(this)
                .count(count)
                .build();
    }
}
