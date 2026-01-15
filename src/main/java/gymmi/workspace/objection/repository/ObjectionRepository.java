package gymmi.workspace.objection.repository;

import gymmi.global.exception.exceptiontype.NotFoundException;
import gymmi.global.exception.message.ErrorCode;
import gymmi.workspace.objection.domain.entity.Objection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ObjectionRepository extends JpaRepository<Objection, Long>, ObjectionCustomRepository {

    @Query("select o from Objection o where o.workoutHistory.id =:workoutHistoryId")
    Optional<Objection> findByWorkoutHistoryId(Long workoutHistoryId);

    default Objection findByIdOrThrow(Long objectionId) {
        Objection objection = findById(objectionId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OBJECTION));
        return objection;
    }

}
