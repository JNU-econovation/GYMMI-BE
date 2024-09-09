package gymmi.entity;

import gymmi.exception.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static gymmi.utils.Regexpressions.REGEX_영어_한글_숫자_만;
import static gymmi.utils.Regexpressions.REGEX_영어_한글_쉼표_만;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace {

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

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @ColumnDefault("''")
    private String tag;

    @OneToMany
    private List<Worker> workers = new ArrayList<>();

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
        this.description = validateDescription(description);
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
            throw new InvalidNumberException("미션 점수는 10점 단위로 입력해주세요.");
        }
        return goalScore;
    }

    private String validateTag(String tag) {
        if (!StringUtils.hasText(tag)) {
            return "";
        }
        if (!REGEX_WORKSPACE_TAG.matcher(tag).matches()) {
            throw new InvalidPatternException("태그는 한글, 영어만 가능합니다.");
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

    public boolean canJoin(User user, String password) {
        if (!matchesPassword(password)) {
            throw new NotMatchedException("비밀번호가 일치하지 않습니다.");
        }
        if (!isPreparing()) {
            throw new InvalidStateException("준비중인 워크스페이스에만 참여할 수 있습니다.");
        }
        if (isFull1()) {
            throw new InvalidStateException("워크스페이스 인원이 가득 찼습니다.");
        }
        if (workers.stream()
                .filter(w -> w.getUser().equals(user))
                .findAny().isPresent()) {
            throw new AlreadyExistException("이미 참여한 워크스페이스 입니다.");
        }
        return true;
    }

    public Worker join(User user, String password) {
        canJoin(user, password);

        return Worker.builder()
                .workspace(this)
                .user(user)
                .build();
        // 사이드 이펙트 제거 대신 -> 새로고침 필요
    }

    public boolean isInProgress() {
        return this.status == WorkspaceStatus.IN_PROGRESS;
    }

    public boolean hasMission(Mission mission) {
        return this.equals(mission.getWorkspace());
    }

    public boolean isPreparing() {
        return this.status == WorkspaceStatus.PREPARING;
    }

    public boolean isCompleted() {
        return this.status == WorkspaceStatus.COMPLETED;
    }

    public boolean isFull(Integer headCount) {
        return this.headCount <= headCount;
    }

    public boolean isFull1() {
        return workers.size() >= headCount;
    }

    public boolean isCreatedBy(User user) {
        return this.creator.equals(user);
    }

    public boolean achieves(int achievementScore) {
        return this.goalScore <= achievementScore;
    }

    public void complete() {
        this.status = WorkspaceStatus.COMPLETED;
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

    public void editDescription(String description) {
        this.description = validateDescription(description);
    }

    public void editTag(String tag) {
        this.tag = validateTag(tag);
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
