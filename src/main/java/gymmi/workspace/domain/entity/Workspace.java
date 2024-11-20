package gymmi.workspace.domain.entity;

import static gymmi.utils.Regexpressions.REGEX_영어_한글_숫자_만;
import static gymmi.utils.Regexpressions.REGEX_영어_한글_쉼표_만;

import gymmi.entity.TimeEntity;
import gymmi.entity.User;
import gymmi.exceptionhandler.exception.InvalidNumberException;
import gymmi.exceptionhandler.exception.InvalidPatternException;
import gymmi.exceptionhandler.exception.InvalidRangeException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.WorkspaceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Getter
public class Workspace extends TimeEntity {

    public static final int MIN_GOAL_SCORE = 100;
    public static final int MAX_GOAL_SCORE = 1000;

    public static final int MIN_HEAD_COUNT = 2;
    public static final int MAX_HEAD_COUNT = 9;

    private static final Pattern REGEX_WORKSPACE_NAME = REGEX_영어_한글_숫자_만;
    private static final Pattern REGEX_WORKSPACE_TAG = REGEX_영어_한글_쉼표_만;
    private final static SecureRandom random = new SecureRandom();

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
    @ColumnDefault("''")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkspaceStatus status;

    @Column(nullable = false)
    private Integer goalScore;

    @Column(nullable = false)
    private Integer headCount;

    @Column(nullable = false)
    @ColumnDefault("''")
    private String tag;

    @Builder
    public Workspace(
            User creator, String name, String description,
            Integer goalScore, Integer headCount, String tag
    ) {
        this.creator = creator;
        this.name = validateName(name);
        this.goalScore = validateGoalScore(goalScore);
        this.headCount = validateHeadCount(headCount);
        this.tag = validateTag(tag);
        this.description = validateDescription(description);
        this.password = generatePassword();
        this.status = WorkspaceStatus.PREPARING;
    }

    private Integer validateHeadCount(Integer headCount) {
        if (headCount < MIN_HEAD_COUNT || headCount > MAX_HEAD_COUNT) {
            throw new InvalidRangeException(ErrorCode.INVALID_WORKSPACE_HEAD_COUNT);
        }
        return headCount;
    }

    public static String validateName(String name) {
        if (name.length() > 9) {
            throw new InvalidRangeException(ErrorCode.INVALID_WORKSPACE_NAME_LENGTH);
        }
        if (!REGEX_WORKSPACE_NAME.matcher(name).matches()) {
            throw new InvalidPatternException(ErrorCode.INVALID_WORKSPACE_NAME_FORMAT);
        }
        return name;
    }

    private Integer validateGoalScore(Integer goalScore) {
        if (goalScore < MIN_GOAL_SCORE || goalScore > MAX_GOAL_SCORE) {
            throw new InvalidRangeException(ErrorCode.INVALID_WORKSPACE_GOAL_SCORE);
        }

        if (!(goalScore % 10 == 0)) {
            throw new InvalidNumberException(ErrorCode.INVALID_MISSION_SCORE_UNIT);
        }
        return goalScore;
    }

    private String validateTag(String tag) {
        if (!StringUtils.hasText(tag)) {
            return "";
        }
        if (tag.length() > 10) {
            throw new InvalidRangeException(ErrorCode.INVALID_TAG_NAME_LENGTH);
        }
        if (!REGEX_WORKSPACE_TAG.matcher(tag).matches()) {
            throw new InvalidPatternException(ErrorCode.INVALID_TAG_NAME_FORMAT);
        }
        return tag;
    }

    private String validateDescription(String description) {
        if (!StringUtils.hasText(description)) {
            return "";
        }
        return description;
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

    public boolean isCreatedBy(Worker worker) {
        return this.creator.equals(worker.getUser());
    }

    public boolean isMoreThan(int achievementScore) {
        return this.goalScore > achievementScore;
    }

    public void changeStatusTo(WorkspaceStatus status) {
        this.status = status;
    }



}
