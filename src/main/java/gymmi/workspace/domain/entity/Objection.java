package gymmi.workspace.domain.entity;

import gymmi.entity.TimeEntity;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static gymmi.exceptionhandler.message.ErrorCode.NO_WORKOUT_HISTORY_EXIST_IN_WORKSPACE;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Getter
public class Objection extends TimeEntity {

    private static final int PERIOD_HOUR = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "worker_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Worker subject;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_confirmation_id", nullable = false, unique = true)
    private WorkoutConfirmation workoutConfirmation;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private boolean isInProgress;

    @OneToMany(mappedBy = "objection")
    private List<Vote> votes = new ArrayList<>();

    @Builder
    public Objection(Worker subject, WorkoutConfirmation workoutConfirmation, String reason) {
        this.subject = subject;
        this.workoutConfirmation = workoutConfirmation;
        this.reason = reason;
        this.isInProgress = true;
    }

    public boolean hasVoteBy(Worker worker) {
        return votes.stream()
                .map(Vote::getWorker)
                .toList()
                .contains(worker);
    }

    public int getApprovalCount() {
        return votes.stream()
                .filter(Vote::getIsApproved)
                .toList().size();
    }

    public int getVoteCount() {
        return votes.size();
    }

    public int getRejectionCount() {
        return votes.stream()
                .filter(vote -> !vote.getIsApproved())
                .toList().size();
    }

    public void close() {
        this.isInProgress = false;
    }

    public void canBeReadIn(Workspace workspace) {
        if (!subject.isJoinedIn(workspace)) {
            throw new NotHavePermissionException(NO_WORKOUT_HISTORY_EXIST_IN_WORKSPACE);
        }
    }

    public void add(Vote vote) {
        votes.add(vote);
    }

    public LocalDateTime getDeadline() {
        return getCreatedAt().plusHours(PERIOD_HOUR);
    }
}


