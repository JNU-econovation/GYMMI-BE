package gymmi.workspace.repository;

import gymmi.exceptionhandler.exception.NotFoundException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Tackle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TackleRepository extends JpaRepository<Tackle, Long> {

    @Query("select t from Tackle t where t.workoutProof.id =:workoutConfirmationId")
    Optional<Tackle> findByWorkoutConfirmationId(Long workoutConfirmationId);

    default Tackle getByTackleId(Long tackleId) {
        Tackle tackle = findById(tackleId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TACKLE));
        return tackle;
    }



}
