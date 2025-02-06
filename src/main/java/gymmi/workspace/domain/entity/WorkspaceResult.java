package gymmi.workspace.domain.entity;

import gymmi.entity.TimeEntity;
import gymmi.workspace.domain.WorkspaceStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class WorkspaceResult extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "workspace_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Workspace workspace;

    @JoinColumn(name = "winner_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Worker winner;

    @JoinColumn(name = "loser_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Worker loser;

    public WorkspaceResult(Workspace workspace, Worker winner, Worker loser) {
        this.workspace = workspace;
        this.winner = winner;
        this.loser = loser;
    }

    public void apply() {
        workspace.changeStatusTo(WorkspaceStatus.FULLY_COMPLETED);
    }
}
