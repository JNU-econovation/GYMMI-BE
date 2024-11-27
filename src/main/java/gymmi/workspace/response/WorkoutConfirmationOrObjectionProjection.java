package gymmi.workspace.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkoutConfirmationOrObjectionProjection {

    private final Long id;
    private final LocalDateTime createdAt;
    private final String type;

    public WorkoutConfirmationOrObjectionProjection(Long id, LocalDateTime createdAt, String type) {
        this.id = id;
        this.createdAt = createdAt;
        this.type = type;
    }
}
