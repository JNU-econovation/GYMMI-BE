package gymmi.entity;

import gymmi.exception.InvalidNumberException;
import gymmi.exception.InvalidPatternException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import static gymmi.utils.Regexpressions.Workspace.REGEX_WORKSPACE_NAME;
import static gymmi.utils.Regexpressions.Workspace.REGEX_WORKSPACE_TAG;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace {

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

    private String validateName(String name) {
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
        validateTag(tag);
        this.tag = tag;
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

    public boolean matches(String password) {
        return this.password.equals(password);
    }

    public boolean isPreparing() {
        return this.status == WorkspaceStatus.PREPARING;
    }

    public boolean isFull(Integer headCount){
        return this.headCount <= headCount;
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
}
