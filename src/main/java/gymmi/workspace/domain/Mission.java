package gymmi.workspace.domain;

import gymmi.exception.class1.InvalidRangeException;
import gymmi.exception.message.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Mission {

    public static final int MIN_SCORE = 1;
    public static final int MAX_SCORE = 10;
    public static final int MAX_NAME_LENGTH = 20;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "workspace_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Workspace workspace;

    @Column(nullable = false)
    private String name;
    private Integer score;

    @Builder
    public Mission(Workspace workspace, String name, Integer score) {
        this.workspace = workspace;
        this.name = validateName(name);
        this.score = validateScore(score);
    }

    private Integer validateScore(Integer score) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new InvalidRangeException(ErrorCode.INVALID_WORKSPACE_MISSION_SCORE);
        }
        return score;
    }

    private String validateName(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidRangeException(ErrorCode.INVALID_WORKSPACE_MISSION_NAME_LENGTH);
        }
        return name;
    }

    public boolean isRegisteredIn(Workspace workspace) {
        return this.workspace.equals(workspace);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getScore() {
        return score;
    }

    public Workspace getWorkspace() {
        return workspace;
    }
}
