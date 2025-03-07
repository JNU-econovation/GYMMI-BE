package gymmi.workspace.repository;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.repository.custom.ObjectionCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ObjectionRepository extends JpaRepository<Objection, Long>, ObjectionCustomRepository {

    @Query("select o from Objection o where o.workoutConfirmation.id =:workoutConfirmationId")
    Optional<Objection> findByWorkoutConfirmationId(Long workoutConfirmationId);

    default Objection getByObjectionId(Long objectionId) {
        Objection objection = findById(objectionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OBJECTION));
        return objection;
    }

}
