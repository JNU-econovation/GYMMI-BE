package gymmi.workspace.service.domain.objection;

import gymmi.exceptionhandler.exception.AlreadyExistException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.repository.ObjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObjectionAlreadyOpenValidator {

    private final ObjectionRepository objectionRepository;

    public void validate(Long workoutHistoryId) {
        if (objectionRepository.findByWorkoutHistoryId(workoutHistoryId).isPresent()) {
            throw new AlreadyExistException(ErrorCode.ALREADY_OBJECTED);
        }
    }
}
