package gymmi.entity;

import static gymmi.utils.Regexpressions.REGEX_영어_한글_만;
import static gymmi.utils.Regexpressions.REGEX_영어_한글_숫자_만;

import gymmi.exception.InvalidNumberException;
import gymmi.exception.InvalidPatternException;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace {

    private static final Pattern REGEX_WORKSPACE_NAME = REGEX_영어_한글_숫자_만;
    private static final Pattern REGEX_WORKSPACE_TAG = REGEX_영어_한글_만;

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

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private WorkspaceStatus status;

    @Column(nullable = false)
    private Integer goalScore;

    @Column(nullable = false)
    private Integer headCount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private String tag;

    @Builder
    public Workspace(
            User creator, String name, String description,
            Integer goalScore, Integer headCount, String tag
    ) {
        this.creator = creator;
        this.name = validateName(name);
        this.goalScore = validateGoalScore(goalScore);
        this.headCount = headCount;
        this.tag = validateTag(tag);

        this.description = description;

        this.password = generatePassword();
        this.createdAt = LocalDateTime.now();
        this.status = WorkspaceStatus.PREPARING;
    }

    public static String validateName(String name) {
        if (!REGEX_WORKSPACE_NAME.matcher(name).matches()) {
            throw new InvalidPatternException("이름은 한글, 영문, 숫자만 가능합니다.");
        }
        return name;
    }

    private Integer validateGoalScore(Integer goalScore) {
        if (!(goalScore % 10 == 0)) {
            throw new InvalidNumberException("10점 단위로 입력해주세요.");
        }
        return goalScore;
    }

    public void changeTag(String tag) {
        this.tag = validateTag(tag);
    }

    private String validateTag(String tag) {
        if (tag == null) {
            return tag;
        }
        if (!REGEX_WORKSPACE_TAG.matcher(tag).matches()) {
            throw new InvalidPatternException("태그는 한글, 영어만 가능합니다.");
        }
        return tag;
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

    public boolean isRegisteredMission(Mission mission) {
        return this.equals(mission.getWorkspace());
    }

    public boolean isPreparing() {
        return this.status == WorkspaceStatus.PREPARING;
    }

    public boolean isFull(Integer headCount) {
        return this.headCount <= headCount;
    }

    public boolean isCreator(User user) {
        return this.creator.equals(user);
    }

    public void start() {
        this.status = WorkspaceStatus.IN_PROGRESS;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public WorkspaceStatus getStatus() {
        return status;
    }

    public User getCreator() {
        return creator;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getGoalScore() {
        return goalScore;
    }

    public Integer getHeadCount() {
        return headCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getTag() {
        return tag;
    }
}
