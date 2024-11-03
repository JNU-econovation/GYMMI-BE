package gymmi.workspace.domain.entity;

import gymmi.exceptionhandler.exception.InvalidRangeException;
import gymmi.exceptionhandler.exception.NotHavePermissionException;
import gymmi.exceptionhandler.message.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@Getter
public class WorkoutProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String comment;

    public WorkoutProof(String filename, String comment) {
        this.filename = filename;
        this.comment = comment;
    }
}
