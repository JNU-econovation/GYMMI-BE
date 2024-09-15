package gymmi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Mission {

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
        this.name = name;
        this.score = score;
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
