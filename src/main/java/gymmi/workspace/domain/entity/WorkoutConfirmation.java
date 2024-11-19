package gymmi.workspace.domain.entity;

import gymmi.service.ImageUse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@Getter
public class WorkoutConfirmation {

    public static final ImageUse IMAGE_USE = ImageUse.WORKOUT_CONFIRMATION;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String comment;

    public WorkoutConfirmation(String filename, String comment) {
        this.filename = filename;
        this.comment = comment;
    }
}
