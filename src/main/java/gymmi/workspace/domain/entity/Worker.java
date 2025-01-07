package gymmi.workspace.domain.entity;

import gymmi.entity.TimeEntity;
import gymmi.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "workspace_id"})})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Worker extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "workspace_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Workspace workspace;

    @Column(nullable = false)
    private Integer contributedScore;

    @Builder
    public Worker(User user, Workspace workspace) {
        this.user = user;
        this.workspace = workspace;
        this.contributedScore = 0;
    }

    public double getContributedPercent() {
        return Math.round((double) contributedScore / workspace.getGoalScore() * 100 * 100) / 100;
    }

    public void addWorkingScore(Integer workingScore) {
        contributedScore += workingScore;
    }

    public boolean isJoinedIn(Workspace workspace) {
        return this.workspace.equals(workspace);
    }

    public String getNickname() {
        return user.getNickname();
    }

    public boolean isTie(Worker worker) {
        return this.contributedScore.equals(worker.getContributedScore());
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", contributedScore=" + contributedScore +
                '}';
    }
}
