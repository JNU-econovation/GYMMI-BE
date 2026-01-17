package gymmi.workspace.workspace.domain.entity;

import gymmi.global.common.entity.TimeEntity;
import gymmi.user.domain.User;
import gymmi.global.exception.exceptiontype.InvalidStateException;
import gymmi.global.exception.message.ErrorCode;
import gymmi.workspace.workspace.domain.WorkspaceCreationValidator;
import gymmi.workspace.workspace.domain.WorkspaceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.security.SecureRandom;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Getter
public class Workspace extends TimeEntity {

    private static final SecureRandom random = new SecureRandom();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "creator", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkspaceStatus status;

    @Column(nullable = false)
    private Integer goalScore;

    @Column(nullable = false)
    private Integer currentScore;

    @Column(nullable = false)
    private Integer headCount;

    @Column(nullable = false)
    private String tag;

    @Column(nullable = false)
    private String task;

    @Builder
    public Workspace(
            User creator, String name, String description,
            Integer goalScore, Integer headCount, String tag, String task
    ) {
        this.creator = creator;
        this.name = name;
        this.goalScore = goalScore;
        this.headCount = headCount;
        this.tag = setDefaultIfNull(tag);
        this.task = task;
        this.description = setDefaultIfNull(description);
        this.password = generatePassword();
        this.status = WorkspaceStatus.PREPARING;
        this.currentScore = 0;
        validateAll();
    }

    private String setDefaultIfNull(String value) {
        return value == null ? "" : value;
    }

    private void validateAll() {
        WorkspaceCreationValidator.validateName(this.name);
        WorkspaceCreationValidator.validateDescription(this.description);
        WorkspaceCreationValidator.validateHeadCount(this.headCount);
        WorkspaceCreationValidator.validateTag(this.tag);
        WorkspaceCreationValidator.validateGoalScore(this.goalScore);
    }


    private String generatePassword() {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            password.append(random.nextInt(10));
        }
        return password.toString();
    }

    public boolean matchesPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isInProgress() {
        return this.status == WorkspaceStatus.IN_PROGRESS;
    }

    public boolean isPreparing() {
        return this.status == WorkspaceStatus.PREPARING;
    }

    public boolean isCompleted() {
        return this.status == WorkspaceStatus.COMPLETED || this.status == WorkspaceStatus.FULLY_COMPLETED;
    }

    public boolean isFullyCompleted() {
        return this.status == WorkspaceStatus.FULLY_COMPLETED;
    }

    public boolean isCreatedBy(User user) {
        return this.creator.equals(user);
    }

    public boolean isMoreThan(int achievementScore) {
        return this.goalScore > achievementScore;
    }

    public boolean hasReachedGoalScore() {
        return this.goalScore <= this.currentScore;
    }


    public void changeStatusTo(WorkspaceStatus status) {
        this.status = status;
    }

    public void editDescription(String description) {
        this.description = WorkspaceCreationValidator.validateDescription(description);
    }

    public void editTag(String tag) {
        this.tag = WorkspaceCreationValidator.validateTag(tag);
    }

    public void editTask(String task) {
        if (this.task.equals(task)) {
            return;
        }
        if (!isPreparing()) {
            throw new InvalidStateException(ErrorCode.ALREADY_ACTIVATED_WORKSPACE);
        }
        this.task = task;
    }

    public void addCurrentScore(int sum) {
        currentScore += sum;
    }
}
