package gymmi.workspace.repository;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Objection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ObjectionRepository extends JpaRepository<Objection, Long> {

    @Query("select o from Objection o where o.workoutConfirmation.id =:workoutConfirmationId")
    Optional<Objection> findByWorkoutConfirmationId(Long workoutConfirmationId);

    default Objection getByObjectionId(Long tackleId) {
        Objection objection = findById(tackleId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OBJECTION));
        return objection;
    }



}
