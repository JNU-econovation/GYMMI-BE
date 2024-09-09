package gymmi.entity;


import gymmi.exception.NotFoundResourcesException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "workspace_id"})})
@EqualsAndHashCode(of = {"id"})
public class Worker {

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

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "task_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Task task;

    public Worker(User user, Workspace workspace) {
        this.user = user;
        this.workspace = workspace;
        this.contributedScore = 0;
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public Worker(User user, Workspace workspace, Task task) {
        this.user = user;
        this.workspace = workspace;
        this.task = task;
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

    public double getContributedPercent() {
        return Math.round((double) contributedScore / workspace.getGoalScore() * 100 * 100) / 100;
    }

    public void addWorkingScore(Integer workingScore) {
        contributedScore += workingScore;
    }

    public WorkingRecord doMission(Mission mission, Integer count) {
        if (!this.workspace.hasMission(mission)) {
            throw new NotFoundResourcesException("해당 미션이 존재하지 않아요.");
        }
        return WorkingRecord.builder()
                .mission(mission)
                .worker(this)
                .count(count)
                .build();
    }

    public Task getTask() {
        return task;
    }
}
