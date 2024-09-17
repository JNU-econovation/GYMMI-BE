package gymmi.workspace.domain;


import gymmi.entity.TimeEntity;
import gymmi.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @JoinColumn(name = "task_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Task task;

    @Builder
    public Worker(User user, Workspace workspace, Task task) {
        this.user = user;
        this.workspace = workspace;
        this.task = task;
        this.contributedScore = 0;
    }

    public double getContributedPercent() {
        return Math.round((double) contributedScore / workspace.getGoalScore() * 100 * 100) / 100;
    }

    void addWorkingScore(Integer workingScore) {
        contributedScore += workingScore;
    }

    public boolean isJoinedIn(Workspace workspace) {
        return this.workspace.equals(workspace);
    }

    public String getNickname() {
        return user.getNickname();
    }
}
