package gymmi.workspace.domain.entity;

import gymmi.entity.TimeEntity;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static gymmi.exceptionhandler.message.ErrorCode.NO_WORKOUT_HISTORY_EXIST_IN_WORKSPACE;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Getter
public class Tackle extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "worker_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Worker subject;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_proof_id", nullable = false, unique = true)
    private WorkoutProof workoutProof;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private boolean isOpen;

    @OneToMany(mappedBy = "tackle")
    private List<Vote> votes = new ArrayList<>();

    @Builder
    public Tackle(Worker subject, WorkoutProof workoutProof, String reason) {
        this.subject = subject;
        this.workoutProof = workoutProof;
        this.reason = reason;
        this.isOpen = true;
    }

    public boolean hasVoteBy(Worker worker) {
        return votes.stream()
                .map(vote -> vote.getWorker())
                .toList()
                .contains(worker);
    }

    public int getAgreeCount() {
        return votes.stream()
                .filter(vote -> vote.getIsAgree() == true)
                .toList().size();
    }

    public int getVoteCount() {
        return votes.size();
    }

    public int getDisAgreeCount() {
        return votes.stream()
                .filter(vote -> vote.getIsAgree() == false)
                .toList().size();
    }

    public void close() {
        this.isOpen = false;
    }

    public void canBeReadIn(Workspace workspace) {
        if (!subject.isJoinedIn(workspace)) {
            throw new NotHavePermissionException(NO_WORKOUT_HISTORY_EXIST_IN_WORKSPACE);
        }
    }

    public void add(Vote vote) {
        votes.add(vote);
    }

}


